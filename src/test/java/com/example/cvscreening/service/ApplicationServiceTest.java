package com.example.cvscreening.service;

import com.example.cvscreening.model.Cv;
import com.example.cvscreening.model.Kandidat;
import com.example.cvscreening.model.PozicionPune;
import com.example.cvscreening.service.fake.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationServiceTest {

    @Test
    void applySuccess_createsApplication_audit_and_notification() {
        FakeKandidatDao kandidatDao = new FakeKandidatDao();
        FakeCvDao cvDao = new FakeCvDao();
        FakePozicionPuneDao pozicionDao = new FakePozicionPuneDao();
        FakeAplikimDao aplikimDao = new FakeAplikimDao();
        FakeAuditDao auditDao = new FakeAuditDao();
        FakeNjoftimDao njoftimDao = new FakeNjoftimDao();

        ApplicationService service = new ApplicationService(
                kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
        );

        Kandidat k = new Kandidat();
        k.setIdPerdorues(10);
        int idKandidat = kandidatDao.create(k);

        PozicionPune p = new PozicionPune();
        p.setTitulliPozicionit("Java Dev");
        p.setKrijuarNga(99);
        p.setGjendja("HAPUR");
        p.setDataHapjes(LocalDate.now());
        int idPozicion = pozicionDao.create(p);

        Cv cv = new Cv();
        cv.setIdKandidat(idKandidat);
        cv.setEmriSkedarit("cv.pdf");
        int idCv = cvDao.create(cv);

        var result = service.aplikimIRi(idKandidat, idPozicion, idCv);
        assertThat(result).isPresent();

        int idAplikimi = result.get();
        assertThat(aplikimDao.findById(idAplikimi)).isPresent();

        // one audit and one notification for candidate
        assertThat(auditDao.findByPerdoruesId(10)).hasSize(1);
        assertThat(njoftimDao.findByPerdoruesId(10)).hasSize(1);
    }

    @Test
    void applyFails_whenCvDoesNotBelongToKandidat() {
        FakeKandidatDao kandidatDao = new FakeKandidatDao();
        FakeCvDao cvDao = new FakeCvDao();
        FakePozicionPuneDao pozicionDao = new FakePozicionPuneDao();
        FakeAplikimDao aplikimDao = new FakeAplikimDao();
        FakeAuditDao auditDao = new FakeAuditDao();
        FakeNjoftimDao njoftimDao = new FakeNjoftimDao();

        ApplicationService service = new ApplicationService(
                kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
        );

        int idKandidat = kandidatDao.create(new Kandidat());
        int idPozicion = pozicionDao.create(new PozicionPune());

        Cv cv = new Cv();
        cv.setIdKandidat(999); // different candidate
        int idCv = cvDao.create(cv);

        var result = service.aplikimIRi(idKandidat, idPozicion, idCv);
        assertThat(result).isEmpty();
    }

    @Test
    void changeStatus_createsAudit_and_notifiesCandidate() {
        FakeKandidatDao kandidatDao = new FakeKandidatDao();
        FakeCvDao cvDao = new FakeCvDao();
        FakePozicionPuneDao pozicionDao = new FakePozicionPuneDao();
        FakeAplikimDao aplikimDao = new FakeAplikimDao();
        FakeAuditDao auditDao = new FakeAuditDao();
        FakeNjoftimDao njoftimDao = new FakeNjoftimDao();

        ApplicationService service = new ApplicationService(
                kandidatDao, cvDao, pozicionDao, aplikimDao, auditDao, njoftimDao
        );

        Kandidat k = new Kandidat();
        k.setIdPerdorues(77);
        int idKandidat = kandidatDao.create(k);

        // create application directly in fake dao
        var aId = aplikimDao.create(new com.example.cvscreening.model.Aplikim() {{
            setIdKandidat(idKandidat);
            setIdPozicion(1);
            setIdCv(1);
            setGjendja("DERGUAR");
        }});

        boolean ok = service.ndryshoGjendjen(aId, "NE_SHQYRTIM", "po shikohet", 500);
        assertThat(ok).isTrue();

        assertThat(auditDao.findByPerdoruesId(500)).hasSize(1);
        assertThat(njoftimDao.findByPerdoruesId(77)).hasSize(1);
    }
}
