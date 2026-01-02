package com.example.cvscreening.service;

import com.example.cvscreening.dao.*;
import com.example.cvscreening.model.Interviste;
import com.example.cvscreening.model.Njoftim;
import com.example.cvscreening.model.RegjisterAuditi;

import java.time.LocalDate;
import java.util.Optional;

public class InterviewService {

    private final IntervisteDao intervisteDao;
    private final AplikimDao aplikimDao;
    private final KandidatDao kandidatDao;
    private final RegjisterAuditiDao auditDao;
    private final NjoftimDao njoftimDao;

    public InterviewService(
            IntervisteDao intervisteDao,
            AplikimDao aplikimDao,
            KandidatDao kandidatDao,
            RegjisterAuditiDao auditDao,
            NjoftimDao njoftimDao
    ) {
        this.intervisteDao = intervisteDao;
        this.aplikimDao = aplikimDao;
        this.kandidatDao = kandidatDao;
        this.auditDao = auditDao;
        this.njoftimDao = njoftimDao;
    }

    // UML: caktoInterviste(aplikimId, date, ora, vendi, recruiterUserId)
    public Optional<Integer> caktoInterviste(int idAplikimi, LocalDate data, String ora, String vendi, Integer idRekrutues) {
        if (data == null) return Optional.empty();
        if (ora == null || ora.isBlank()) return Optional.empty();

        var aplikimOpt = aplikimDao.findById(idAplikimi);
        if (aplikimOpt.isEmpty()) return Optional.empty();

        // Optional rule: one interview per application (you can allow multiple if you want)
        boolean already = !intervisteDao.findByAplikimId(idAplikimi).isEmpty();
        if (already) return Optional.empty();

        Interviste i = new Interviste();
        i.setIdAplikimi(idAplikimi);
        i.setDataIntervistes(data);
        i.setOra(ora);
        i.setVendi(vendi);
        i.setVleresimiRekrutuesit(null);
        i.setPershtypja(null);
        i.setRezultati("NE_PRITJE");

        int idInterviste = intervisteDao.create(i);

        // Audit
        RegjisterAuditi a = new RegjisterAuditi();
        a.setIdPerdorues(idRekrutues);
        a.setObjekti("INTERVISTE");
        a.setVeprimi("CREATE");
        a.setDataVeprimit(LocalDate.now());
        a.setOraVeprimit(null);
        a.setPershkrimiDetajuar("Interviste e caktuar per aplikim " + idAplikimi);
        auditDao.create(a);

        // Notify candidate
        Integer idKandidat = aplikimOpt.get().getIdKandidat();
        Integer idPerdoruesKandidat = kandidatDao.findById(idKandidat).map(k -> k.getIdPerdorues()).orElse(null);
        if (idPerdoruesKandidat != null) {
            Njoftim n = new Njoftim();
            n.setIdPerdorues(idPerdoruesKandidat);
            n.setMenyraDergimit("SISTEM");
            n.setPermbajtja("Intervista u caktua me date " + data + " ne oren " + ora + ".");
            n.setDataDergimit(LocalDate.now());
            n.setStatusiLeximit("PALEXUAR");
            njoftimDao.create(n);
        }

        return Optional.of(idInterviste);
    }

    // UML: vendosRezultatin(intervisteId, rezultat, vleresim, pershtypje, recruiterUserId)
    public boolean vendosRezultatin(int idInterviste, String rezultat, Integer vleresim, String pershtypje, Integer idRekrutues) {
        if (rezultat == null || rezultat.isBlank()) return false;

        var opt = intervisteDao.findById(idInterviste);
        if (opt.isEmpty()) return false;

        boolean ok = intervisteDao.updateRezultat(idInterviste, rezultat, vleresim, pershtypje);
        if (!ok) return false;

        // Audit
        RegjisterAuditi a = new RegjisterAuditi();
        a.setIdPerdorues(idRekrutues);
        a.setObjekti("INTERVISTE");
        a.setVeprimi("UPDATE_RESULT");
        a.setDataVeprimit(LocalDate.now());
        a.setOraVeprimit(null);
        a.setPershkrimiDetajuar("Rezultati u vendos: " + rezultat + " per interviste " + idInterviste);
        auditDao.create(a);

        // Notify candidate
        int idAplikimi = opt.get().getIdAplikimi();
        var aplikimOpt = aplikimDao.findById(idAplikimi);
        if (aplikimOpt.isPresent()) {
            Integer idKandidat = aplikimOpt.get().getIdKandidat();
            Integer idPerdoruesKandidat = kandidatDao.findById(idKandidat).map(k -> k.getIdPerdorues()).orElse(null);
            if (idPerdoruesKandidat != null) {
                Njoftim n = new Njoftim();
                n.setIdPerdorues(idPerdoruesKandidat);
                n.setMenyraDergimit("SISTEM");
                n.setPermbajtja("Rezultati i intervistes u perditesua: " + rezultat);
                n.setDataDergimit(LocalDate.now());
                n.setStatusiLeximit("PALEXUAR");
                njoftimDao.create(n);
            }
        }

        return true;
    }
}
