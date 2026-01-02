package com.example.cvscreening.service;

import com.example.cvscreening.dao.*;
import com.example.cvscreening.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ScreeningService {

    private final KandidatDao kandidatDao;
    private final CvDao cvDao;
    private final PozicionPuneDao pozicionDao;
    private final AplikimDao aplikimDao;
    private final RegjisterAuditiDao auditDao;
    private final NjoftimDao njoftimDao;

    public ScreeningService(
            KandidatDao kandidatDao,
            CvDao cvDao,
            PozicionPuneDao pozicionDao,
            AplikimDao aplikimDao,
            RegjisterAuditiDao auditDao,
            NjoftimDao njoftimDao
    ) {
        this.kandidatDao = kandidatDao;
        this.cvDao = cvDao;
        this.pozicionDao = pozicionDao;
        this.aplikimDao = aplikimDao;
        this.auditDao = auditDao;
        this.njoftimDao = njoftimDao;
    }

    // UML-ish: llogaritPerputhshmerine(aplikimId, recruiterUserId)
    public Optional<Double> llogaritPerputhshmerine(int idAplikimi, Integer idRekrutues) {
        var aplikimOpt = aplikimDao.findById(idAplikimi);
        if (aplikimOpt.isEmpty()) return Optional.empty();

        Aplikim a = aplikimOpt.get();

        var kandidatOpt = kandidatDao.findById(a.getIdKandidat());
        var pozicionOpt = pozicionDao.findById(a.getIdPozicion());
        var cvOpt = cvDao.findById(a.getIdCv());

        if (kandidatOpt.isEmpty() || pozicionOpt.isEmpty() || cvOpt.isEmpty()) return Optional.empty();

        Kandidat k = kandidatOpt.get();
        PozicionPune p = pozicionOpt.get();

        double score = score(k, p);

        // store score
        boolean ok1 = cvDao.updateMatchScore(a.getIdCv(), score);
        boolean ok2 = aplikimDao.updateScore(idAplikimi, score);
        if (!ok1 || !ok2) return Optional.empty();

        // audit
        RegjisterAuditi audit = new RegjisterAuditi();
        audit.setIdPerdorues(idRekrutues);
        audit.setObjekti("SCREENING");
        audit.setVeprimi("CALCULATE_SCORE");
        audit.setDataVeprimit(LocalDate.now());
        audit.setOraVeprimit(null);
        audit.setPershkrimiDetajuar("Score " + score + " per aplikim " + idAplikimi);
        auditDao.create(audit);

        // notify candidate (optional)
        Integer perdoruesKandidat = kandidatDao.findById(a.getIdKandidat()).map(Kandidat::getIdPerdorues).orElse(null);
        if (perdoruesKandidat != null) {
            Njoftim n = new Njoftim();
            n.setIdPerdorues(perdoruesKandidat);
            n.setMenyraDergimit("SISTEM");
            n.setPermbajtja("Aplikimi juaj u vleresua (screening) me score: " + format(score));
            n.setDataDergimit(LocalDate.now());
            n.setStatusiLeximit("PALEXUAR");
            njoftimDao.create(n);
        }

        return Optional.of(score);
    }

    // -------------------------
    // Scoring algorithm (explainable)
    // -------------------------
    private double score(Kandidat k, PozicionPune p) {
        Set<String> kriteret = tokenize(p.getKriteret());
        if (kriteret.isEmpty()) return 0.0;

        Set<String> candidateTokens = new HashSet<>();
        candidateTokens.addAll(tokenize(k.getAftesiteProfesionale()));
        candidateTokens.addAll(tokenize(k.getGjuhetEHuaja()));
        candidateTokens.addAll(tokenize(k.getFushaStudimit()));
        candidateTokens.addAll(tokenize(k.getNiveliArsimor()));
        candidateTokens.addAll(tokenize(k.getVendbanimi()));

        // keyword match ratio
        long matched = kriteret.stream().filter(candidateTokens::contains).count();
        double keywordScore = (double) matched / kriteret.size(); // 0..1

        // experience bonus (0..1, capped)
        int years = k.getViteEksperience() == null ? 0 : k.getViteEksperience();
        double expScore = Math.min(1.0, years / 5.0); // 5+ years => 1.0

        // weights
        double finalScore = 0.75 * keywordScore + 0.25 * expScore;

        // round to 2 decimals
        return Math.round(finalScore * 100.0) / 100.0;
    }

    private Set<String> tokenize(String text) {
        if (text == null) return Set.of();
        return Arrays.stream(text.toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", " ")
                        .split("\\s+"))
                .filter(s -> !s.isBlank())
                .filter(s -> s.length() >= 2)
                .collect(Collectors.toSet());
    }

    private String format(double x) {
        return String.format(Locale.US, "%.2f", x);
    }
}
