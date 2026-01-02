package com.example.cvscreening;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.*;
        import com.example.cvscreening.dao.jdbc.*;
        import com.example.cvscreening.model.*;
        import com.example.cvscreening.service.*;

        import java.sql.Connection;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) throws Exception {
        try (Connection conn = Db.connect()) {
            Db.runSchema(conn);

            // --- DAOs ---
            PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
            KandidatDao kandidatDao = new KandidatDaoJdbc(conn);
            CvDao cvDao = new CvDaoJdbc(conn);
            PozicionPuneDao pozicionDao = new PozicionPuneDaoJdbc(conn);
            AplikimDao aplikimDao = new AplikimDaoJdbc(conn);
            IntervisteDao intervisteDao = new IntervisteDaoJdbc(conn);
            NjoftimDao njoftimDao = new NjoftimDaoJdbc(conn);
            RaportDao raportDao = new RaportDaoJdbc(conn);
            RegjisterAuditiDao auditDao = new RegjisterAuditiDaoJdbc(conn);

            // --- Services ---
            AuthService authService = new AuthService(perdoruesDao);
            ApplicationService applicationService = new ApplicationService(
                    kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
            );
            InterviewService interviewService = new InterviewService(
                    intervisteDao, aplikimDao, kandidatDao, auditDao, njoftimDao
            );
            NotificationService notificationService = new NotificationService(njoftimDao);
            ReportService reportService = new ReportService(raportDao);
            ScreeningService screeningService = new ScreeningService(
                    kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
            );

            System.out.println("=== CV Screening Demo (H2) ===");

            // 1) Create recruiter user
            Perdorues recruiter = new Perdorues();
            recruiter.setEmri("Recruiter");
            recruiter.setMbiemri("One");
            recruiter.setAdresaEmail("recruiter@test.com");
            recruiter.setFjalekalimi("secret123");
            recruiter.setRoli("REKRUTUES");
            recruiter.setDataRegjistrimit(LocalDate.now());
            recruiter.setGjendja("AKTIV");
            int idRecruiter = perdoruesDao.create(recruiter);

            // 2) Create candidate user
            Perdorues user = new Perdorues();
            user.setEmri("Gloria");
            user.setMbiemri("Doda");
            user.setAdresaEmail("gloria@test.com");
            user.setFjalekalimi("pass123");
            user.setRoli("KANDIDAT");
            user.setDataRegjistrimit(LocalDate.now());
            user.setGjendja("AKTIV");
            int idPerdorues = perdoruesDao.create(user);

            // 3) Candidate profile (Kandidat)
            Kandidat k = new Kandidat();
            k.setIdPerdorues(idPerdorues);
            k.setViteEksperience(2);
            k.setAftesiteProfesionale("Java SQL Spring");
            k.setGjuhetEHuaja("EN");
            k.setFushaStudimit("Computer Engineering");
            int idKandidat = kandidatDao.create(k);

            // 4) CV
            Cv cv = new Cv();
            cv.setIdKandidat(idKandidat);
            cv.setEmriSkedarit("cv_gloria.pdf");
            cv.setFormatiSkedarit("PDF");
            cv.setDataNgarkimit(LocalDate.now());
            int idCv = cvDao.create(cv);

            // 5) Job Position
            PozicionPune poz = new PozicionPune();
            poz.setTitulliPozicionit("Java Developer");
            poz.setDepartamenti("IT");
            poz.setKriteret("Java SQL Spring");
            poz.setDataHapjes(LocalDate.now());
            poz.setDataAplikimit(LocalDate.now().plusDays(30));
            poz.setGjendja("HAPUR");
            poz.setKrijuarNga(idRecruiter);
            int idPozicion = pozicionDao.create(poz);

            // 6) Login demo
            System.out.println("\n--- Login demo ---");
            System.out.println("Login Gloria: " + authService.login("gloria@test.com", "pass123").isPresent());
            System.out.println("Login wrong password: " + authService.login("gloria@test.com", "wrong").isPresent());

            // 7) Apply to job
            System.out.println("\n--- Apply demo ---");
            Integer idAplikimi = applicationService.aplikimIRi(idKandidat, idPozicion, idCv).orElse(null);
            System.out.println("Aplikim created id: " + idAplikimi);

            // 8) Screening score
            System.out.println("\n--- Screening demo ---");
            Double score = screeningService.llogaritPerputhshmerine(idAplikimi, idRecruiter).orElse(null);
            System.out.println("Score: " + score);

            // 9) Change application status
            System.out.println("\n--- Status update demo ---");
            boolean statusOk = applicationService.ndryshoGjendjen(idAplikimi, "NE_SHQYRTIM", "Duket mire", idRecruiter);
            System.out.println("Status updated: " + statusOk);

            // 10) Schedule interview
            System.out.println("\n--- Interview demo ---");
            Integer idInterviste = interviewService.caktoInterviste(
                    idAplikimi, LocalDate.now().plusDays(3), "10:00", "Online", idRecruiter
            ).orElse(null);
            System.out.println("Interviste created id: " + idInterviste);

            // 11) Set interview result
            boolean resultOk = interviewService.vendosRezultatin(idInterviste, "KALON", 9, "Shume mire", idRecruiter);
            System.out.println("Interviste result set: " + resultOk);

            // 12) Generate report
            System.out.println("\n--- Report demo ---");
            Integer idRaport = reportService.gjeneroRaport(idRecruiter, "KPI", "Raport demo", "PDF").orElse(null);
            System.out.println("Raport created id: " + idRaport);

            // 13) Show notifications for candidate
            System.out.println("\n--- Candidate notifications ---");
            for (Njoftim n : notificationService.merrNjoftimePerPerdorues(idPerdorues)) {
                System.out.println("* [" + n.getStatusiLeximit() + "] " + n.getPermbajtja());
            }

            // Mark first notification as read (if exists)
            var list = notificationService.merrNjoftimePerPerdorues(idPerdorues);
            if (!list.isEmpty()) {
                boolean readOk = notificationService.shenoSiLexuar(list.get(0).getIdNjoftim());
                System.out.println("Marked first as read: " + readOk);
            }

            System.out.println("\n=== Demo finished OK ===");
        }
    }
}
