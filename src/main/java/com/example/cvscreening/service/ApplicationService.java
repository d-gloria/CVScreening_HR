package com.example.cvscreening.service;

import com.example.cvscreening.dao.*;
import com.example.cvscreening.model.Aplikim;
import com.example.cvscreening.model.Njoftim;
import com.example.cvscreening.model.RegjisterAuditi;

import java.time.LocalDate;
import java.util.Optional;

public class ApplicationService {

    private final KandidatDao kandidatDao;
    private final CvDao cvDao;
    private final PozicionPuneDao pozicionDao;
    private final AplikimDao aplikimDao;
    private final RegjisterAuditiDao auditDao;
    private final NjoftimDao njoftimDao;

    public ApplicationService(
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

    // UML: aplikimIRi(kandidatId, pozicionId, cvId)
    public Optional<Integer> aplikimIRi(int idKandidat, int idPozicion, int idCv) {
        // validate parent entities exist
        if (kandidatDao.findById(idKandidat).isEmpty()) return Optional.empty();
        if (pozicionDao.findById(idPozicion).isEmpty()) return Optional.empty();

        var cvOpt = cvDao.findById(idCv);
        if (cvOpt.isEmpty()) return Optional.empty();

        // CV must belong to this candidate
        if (!Integer.valueOf(idKandidat).equals(cvOpt.get().getIdKandidat())) return Optional.empty();

        // prevent duplicate application (same kandidat + pozicion)
        boolean alreadyApplied = aplikimDao.findByKandidatId(idKandidat).stream()
                .anyMatch(a -> Integer.valueOf(idPozicion).equals(a.getIdPozicion()));
        if (alreadyApplied) return Optional.empty();

        // create application
        Aplikim a = new Aplikim();
        a.setIdKandidat(idKandidat);
        a.setIdPozicion(idPozicion);
        a.setIdCv(idCv);
        a.setDataAplikimit(LocalDate.now());
        a.setGjendja("DERGUAR");
        a.setVleresimi(null);
        a.setKomentetRekrutuesit(null);

        int idAplikimi = aplikimDao.create(a);

        // audit log (candidate action)
        RegjisterAuditi audit = new RegjisterAuditi();
        audit.setIdPerdorues(resolvePerdoruesIdForKandidat(idKandidat).orElse(null));
        audit.setObjekti("APLIKIM");
        audit.setVeprimi("CREATE");
        audit.setDataVeprimit(LocalDate.now());
        audit.setOraVeprimit(null);
        audit.setPershkrimiDetajuar("Aplikim i ri per pozicion " + idPozicion);
        auditDao.create(audit);

        // notification to candidate (optional but nice)
        Integer idPerdorues = resolvePerdoruesIdForKandidat(idKandidat).orElse(null);
        if (idPerdorues != null) {
            Njoftim n = new Njoftim();
            n.setIdPerdorues(idPerdorues);
            n.setMenyraDergimit("SISTEM");
            n.setPermbajtja("Aplikimi u dergua me sukses.");
            n.setDataDergimit(LocalDate.now());
            n.setStatusiLeximit("PALEXUAR");
            njoftimDao.create(n);
        }

        return Optional.of(idAplikimi);
    }

    // UML: ndryshoGjendjen(aplikimId, status, koment)
    public boolean ndryshoGjendjen(int idAplikimi, String statusIRi, String komentRekrutuesi, Integer idPerdoruesVeprues) {
        if (statusIRi == null || statusIRi.isBlank()) return false;

        var opt = aplikimDao.findById(idAplikimi);
        if (opt.isEmpty()) return false;

        Aplikim a = opt.get();
        a.setGjendja(statusIRi);
        a.setKomentetRekrutuesit(komentRekrutuesi);

        boolean updated = aplikimDao.updateStatusAndComment(idAplikimi, statusIRi, komentRekrutuesi);
        if (!updated) return false;

        // audit log (recruiter action)
        RegjisterAuditi audit = new RegjisterAuditi();
        audit.setIdPerdorues(idPerdoruesVeprues);
        audit.setObjekti("APLIKIM");
        audit.setVeprimi("UPDATE_STATUS");
        audit.setDataVeprimit(LocalDate.now());
        audit.setOraVeprimit(null);
        audit.setPershkrimiDetajuar("Gjendja u ndryshua ne " + statusIRi + " per aplikim " + idAplikimi);
        auditDao.create(audit);

        // notify candidate
        Integer kandidatPerdorues = resolvePerdoruesIdForKandidat(a.getIdKandidat()).orElse(null);
        if (kandidatPerdorues != null) {
            Njoftim n = new Njoftim();
            n.setIdPerdorues(kandidatPerdorues);
            n.setMenyraDergimit("SISTEM");
            n.setPermbajtja("Gjendja e aplikimit tuaj u perditesua: " + statusIRi);
            n.setDataDergimit(LocalDate.now());
            n.setStatusiLeximit("PALEXUAR");
            njoftimDao.create(n);
        }

        return true;
    }

    private Optional<Integer> resolvePerdoruesIdForKandidat(Integer idKandidat) {
        if (idKandidat == null) return Optional.empty();
        return kandidatDao.findById(idKandidat).map(k -> k.getIdPerdorues());
    }
}
