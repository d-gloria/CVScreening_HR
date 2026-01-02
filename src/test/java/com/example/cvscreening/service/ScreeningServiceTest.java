package com.example.cvscreening.service;

import com.example.cvscreening.model.*;
import com.example.cvscreening.service.fake.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScreeningServiceTest {

    @Test
    void calculateScore_updatesCvAndApplication_and_createsNotification() {
        FakeKandidatDao kandidatDao = new FakeKandidatDao();
        FakeCvDao cvDao = new FakeCvDao();
        FakePozicionPuneDao pozicionDao = new FakePozicionPuneDao();
        FakeAplikimDao aplikimDao = new FakeAplikimDao();
        FakeAuditDao auditDao = new FakeAuditDao();
        FakeNjoftimDao njoftimDao = new FakeNjoftimDao();

        ScreeningService service = new ScreeningService(
                kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
        );

        Kandidat k = new Kandidat();
        k.setIdPerdorues(55);
        k.setAftesiteProfesionale("Java SQL Spring");
        k.setGjuhetEHuaja("EN");
        k.setViteEksperience(2);
        int idKandidat = kandidatDao.create(k);

        Cv cv = new Cv();
        cv.setIdKandidat(idKandidat);
        cv.setEmriSkedarit("cv.pdf");
        int idCv = cvDao.create(cv);

        PozicionPune p = new PozicionPune();
        p.setKriteret("Java SQL");
        int idPozicion = pozicionDao.create(p);

        Aplikim a = new Aplikim();
        a.setIdKandidat(idKandidat);
        a.setIdCv(idCv);
        a.setIdPozicion(idPozicion);
        int idAplikimi = aplikimDao.create(a);

        var scoreOpt = service.llogaritPerputhshmerine(idAplikimi, 900);
        assertThat(scoreOpt).isPresent();

        double score = scoreOpt.get();
        assertThat(score).isBetween(0.0, 1.0);

        var updatedCv = cvDao.findById(idCv).orElseThrow();
        assertThat(updatedCv.getPerputhshmeriaMePozicionin()).isEqualTo(score);

        var updatedA = aplikimDao.findById(idAplikimi).orElseThrow();
        assertThat(updatedA.getVleresimi()).isEqualTo(score);

        assertThat(auditDao.findByPerdoruesId(900)).hasSize(1);
        assertThat(njoftimDao.findByPerdoruesId(55)).hasSize(1);
    }
}
