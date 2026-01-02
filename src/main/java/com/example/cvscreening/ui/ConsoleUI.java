package com.example.cvscreening.ui;

import com.example.cvscreening.dao.*;
import com.example.cvscreening.model.*;
import com.example.cvscreening.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {

    private final AuthService authService;
    private final ApplicationService applicationService;
    private final ScreeningService screeningService;
    private final InterviewService interviewService;
    private final NotificationService notificationService;
    private final ReportService reportService;

    private final PerdoruesDao perdoruesDao;
    private final KandidatDao kandidatDao;
    private final CvDao cvDao;
    private final PozicionPuneDao pozicionDao;
    private final AplikimDao aplikimDao;

    private final Scanner in = new Scanner(System.in);

    private Perdorues currentUser;

    public ConsoleUI(
            AuthService authService,
            ApplicationService applicationService,
            ScreeningService screeningService,
            InterviewService interviewService,
            NotificationService notificationService,
            ReportService reportService,
            PerdoruesDao perdoruesDao,
            KandidatDao kandidatDao,
            CvDao cvDao,
            PozicionPuneDao pozicionDao,
            AplikimDao aplikimDao
    ) {
        this.authService = authService;
        this.applicationService = applicationService;
        this.screeningService = screeningService;
        this.interviewService = interviewService;
        this.notificationService = notificationService;
        this.reportService = reportService;
        this.perdoruesDao = perdoruesDao;
        this.kandidatDao = kandidatDao;
        this.cvDao = cvDao;
        this.pozicionDao = pozicionDao;
        this.aplikimDao = aplikimDao;
    }

    public void run() {
        System.out.println("CV Screening System");
        seedIfEmpty();

        while (true) {
            if (currentUser == null) {
                showGuestMenu();
            } else if ("REKRUTUES".equalsIgnoreCase(currentUser.getRoli())) {
                showRecruiterMenu();
            } else {
                showCandidateMenu();
            }
        }
    }

    // ---------------- MENUS ----------------

    private void showGuestMenu() {
        System.out.println("\n ===Guest Menu===");
        System.out.println("1) Register candidate");
        System.out.println("2) Login");
        System.out.println("3) View job positions");
        System.out.println("0) Exit");

        String choice = ask("Choose");
        switch (choice) {
            case "1" -> registerCandidate();
            case "2" -> login();
            case "3" -> listJobs();
            case "0" -> exit();
            default -> System.out.println("Invalid option");
        }
    }

    private void showCandidateMenu() {
        System.out.println("\n=== Candidate Menu (" + currentUser.getAdresaEmail() + ") ===");
        System.out.println("1) View job positions");
        System.out.println("2) Apply to a job");
        System.out.println("3) View my applications");
        System.out.println("4) View my notifications");
        System.out.println("5) Logout");
        System.out.println("0) Exit");

        String choice = ask("Choose");
        switch (choice) {
            case "1" -> listJobs();
            case "2" -> applyToJob();
            case "3" -> myApplications();
            case "4" -> myNotifications();
            case "5" -> logout();
            case "0" -> exit();
            default -> System.out.println("Invalid option");
        }
    }

    private void showRecruiterMenu() {
        System.out.println("\n=== Recruiter Menu (" + currentUser.getAdresaEmail() + ") ===");
        System.out.println("1) View job positions");
        System.out.println("2) View applications for a job");
        System.out.println("3) Run screening for an application");
        System.out.println("4) Schedule interview");
        System.out.println("5) Set interview result");
        System.out.println("6) Generate report");
        System.out.println("7) Logout");
        System.out.println("0) Exit");

        String choice = ask("Choose");
        switch (choice) {
            case "1" -> listJobs();
            case "2" -> listApplicationsForJob();
            case "3" -> runScreening();
            case "4" -> scheduleInterview();
            case "5" -> setInterviewResult();
            case "6" -> generateReport();
            case "7" -> logout();
            case "0" -> exit();
            default -> System.out.println("Invalid option");
        }
    }

    // ---------------- ACTIONS ----------------

    private void registerCandidate() {
        System.out.println("\n--- Register Candidate ---");
        String emri = ask("Emri");
        String mbiemri = ask("Mbiemri");
        String email = ask("Email");
        String pass = ask("Password");

        Perdorues u = new Perdorues();
        u.setEmri(emri);
        u.setMbiemri(mbiemri);
        u.setAdresaEmail(email);
        u.setFjalekalimi(pass);
        u.setRoli("KANDIDAT");
        u.setGjendja("AKTIV");
        u.setDataRegjistrimit(LocalDate.now());

        int idPerdorues = perdoruesDao.create(u);

        Kandidat k = new Kandidat();
        k.setIdPerdorues(idPerdorues);
        k.setAftesiteProfesionale(ask("Aftesite (e.g. Java SQL Spring)"));
        String yearsStr = ask("Vite eksperience (number)");
        try { k.setViteEksperience(Integer.parseInt(yearsStr)); } catch (Exception ignored) { k.setViteEksperience(0); }
        k.setGjuhetEHuaja(ask("Gjuhet (e.g. EN)"));
        k.setFushaStudimit(ask("Fusha studimit"));
        int idKandidat = kandidatDao.create(k);

        Cv cv = new Cv();
        cv.setIdKandidat(idKandidat);
        cv.setEmriSkedarit(ask("CV filename (e.g. cv.pdf)"));
        cv.setFormatiSkedarit("PDF");
        cv.setDataNgarkimit(LocalDate.now());
        int idCv = cvDao.create(cv);

        System.out.println("Registered OK. idPerdorues=" + idPerdorues + ", idKandidat=" + idKandidat + ", idCv=" + idCv);
    }

    private void login() {
        System.out.println("\n--- Login ---");
        String email = ask("Email");
        String pass = ask("Password");
        Optional<Perdorues> u = authService.login(email, pass);
        if (u.isPresent()) {
            currentUser = u.get();
            System.out.println("Login OK. Role=" + currentUser.getRoli());
        } else {
            System.out.println("Login failed.");
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }

    private void listJobs() {
        System.out.println("\n--- Job Positions ---");
        List<PozicionPune> list = pozicionDao.findAll();
        if (list.isEmpty()) {
            System.out.println("(No jobs)");
            return;
        }
        for (PozicionPune p : list) {
            System.out.println(p.getIdPozicion() + ") " + p.getTitulliPozicionit()
                    + " | Dept=" + p.getDepartamenti()
                    + " | Status=" + p.getGjendja());
        }
    }

    private void applyToJob() {
        Integer idKandidat = kandidatDao.findByPerdoruesId(currentUser.getIdPerdorues())
                .map(Kandidat::getIdKandidat).orElse(null);

        if (idKandidat == null) {
            System.out.println("No kandidat profile found.");
            return;
        }

        List<Cv> cvs = cvDao.findByKandidatId(idKandidat);
        if (cvs.isEmpty()) {
            System.out.println("No CV found for this kandidat.");
            return;
        }

        int idPozicion;
        try {
            idPozicion = Integer.parseInt(ask("Enter job id"));
        } catch (Exception e) {
            System.out.println("Invalid job id.");
            return;
        }

        int idCv = cvs.get(0).getIdCv(); // for demo: first CV

        var res = applicationService.aplikimIRi(idKandidat, idPozicion, idCv);
        System.out.println(res.isPresent() ? "Applied OK. idAplikimi=" + res.get() : "Apply failed.");
    }

    private void myApplications() {
        Integer idKandidat = kandidatDao.findByPerdoruesId(currentUser.getIdPerdorues())
                .map(Kandidat::getIdKandidat).orElse(null);
        if (idKandidat == null) {
            System.out.println("No kandidat profile found.");
            return;
        }

        var list = aplikimDao.findByKandidatId(idKandidat);
        if (list.isEmpty()) {
            System.out.println("(No applications)");
            return;
        }

        for (Aplikim a : list) {
            System.out.println("Aplikim " + a.getIdAplikimi()
                    + " | pozicion=" + a.getIdPozicion()
                    + " | status=" + a.getGjendja()
                    + " | score=" + a.getVleresimi());
        }
    }

    private void myNotifications() {
        var list = notificationService.merrNjoftimePerPerdorues(currentUser.getIdPerdorues());
        if (list.isEmpty()) {
            System.out.println("(No notifications)");
            return;
        }
        for (Njoftim n : list) {
            System.out.println(n.getIdNjoftim() + ") [" + n.getStatusiLeximit() + "] " + n.getPermbajtja());
        }

        String idStr = ask("Mark one as read? Enter id or blank");
        if (!idStr.isBlank()) {
            try {
                boolean ok = notificationService.shenoSiLexuar(Integer.parseInt(idStr));
                System.out.println(ok ? "Marked read." : "Failed.");
            } catch (Exception e) {
                System.out.println("Invalid id.");
            }
        }
    }

    private void listApplicationsForJob() {
        int idPozicion;
        try {
            idPozicion = Integer.parseInt(ask("Enter job id"));
        } catch (Exception e) {
            System.out.println("Invalid job id.");
            return;
        }

        var list = aplikimDao.findByPozicionId(idPozicion);
        if (list.isEmpty()) {
            System.out.println("(No applications for this job)");
            return;
        }

        for (Aplikim a : list) {
            System.out.println("Aplikim " + a.getIdAplikimi()
                    + " | kandidat=" + a.getIdKandidat()
                    + " | status=" + a.getGjendja()
                    + " | score=" + a.getVleresimi());
        }
    }

    private void runScreening() {
        int idAplikimi;
        try {
            idAplikimi = Integer.parseInt(ask("Enter application id"));
        } catch (Exception e) {
            System.out.println("Invalid application id.");
            return;
        }

        var res = screeningService.llogaritPerputhshmerine(idAplikimi, currentUser.getIdPerdorues());
        System.out.println(res.isPresent() ? "Score=" + res.get() : "Screening failed.");
    }

    private void scheduleInterview() {
        int idAplikimi;
        try {
            idAplikimi = Integer.parseInt(ask("Enter application id"));
        } catch (Exception e) {
            System.out.println("Invalid application id.");
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(ask("Date (YYYY-MM-DD)"));
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        String ora = ask("Time (e.g. 10:00)");
        String vendi = ask("Place (e.g. Online)");

        var res = interviewService.caktoInterviste(idAplikimi, date, ora, vendi, currentUser.getIdPerdorues());
        System.out.println(res.isPresent() ? "Interview created id=" + res.get() : "Failed to schedule.");
    }

    private void setInterviewResult() {
        int idInterviste;
        try {
            idInterviste = Integer.parseInt(ask("Enter interview id"));
        } catch (Exception e) {
            System.out.println("Invalid interview id.");
            return;
        }

        String rezultat = ask("Result (KALON / NUK_KALON / NE_PRITJE)");
        String vStr = ask("Score (0-10) blank for null");
        Integer vleresim = null;
        if (!vStr.isBlank()) {
            try { vleresim = Integer.parseInt(vStr); } catch (Exception ignored) { vleresim = null; }
        }
        String pershtypje = ask("Notes");

        boolean ok = interviewService.vendosRezultatin(idInterviste, rezultat, vleresim, pershtypje, currentUser.getIdPerdorues());
        System.out.println(ok ? "Updated." : "Failed.");
    }

    private void generateReport() {
        String type = ask("Report type (e.g. KPI)");
        String desc = ask("Description");
        String format = ask("Format (PDF/XLSX) blank=>PDF");

        var res = reportService.gjeneroRaport(currentUser.getIdPerdorues(), type, desc, format);
        System.out.println(res.isPresent() ? "Report record id=" + res.get() : "Failed.");
    }

    // ---------------- HELPERS ----------------

    private String ask(String label) {
        System.out.print(label + ": ");
        return in.nextLine().trim();
    }

    private void exit() {
        System.out.println("Bye.");
        System.exit(0);
    }

    private void seedIfEmpty() {
        if (!pozicionDao.findAll().isEmpty()) return;

        Perdorues r = new Perdorues();
        r.setEmri("Recruiter");
        r.setMbiemri("Seed");
        r.setAdresaEmail("recruiter@seed.com");
        r.setFjalekalimi("secret123");
        r.setRoli("REKRUTUES");
        r.setGjendja("AKTIV");
        r.setDataRegjistrimit(LocalDate.now());
        int idRec = perdoruesDao.create(r);

        PozicionPune p1 = new PozicionPune();
        p1.setTitulliPozicionit("Java Developer");
        p1.setDepartamenti("IT");
        p1.setKriteret("Java SQL Spring");
        p1.setDataHapjes(LocalDate.now());
        p1.setDataAplikimit(LocalDate.now().plusDays(30));
        p1.setGjendja("HAPUR");
        p1.setKrijuarNga(idRec);
        pozicionDao.create(p1);

        System.out.println("(Seeded 1 recruiter + 1 job position for demo)");
    }
}
