package com.example.cvscreening.service;

import com.example.cvscreening.model.Aplikim;
import com.example.cvscreening.model.Kandidat;
import com.example.cvscreening.service.fake.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InterviewServiceTest {

    @Test
    void scheduleInterview_success_createsAuditAndNotification() {
        FakeIntervisteDao intervisteDao = new FakeIntervisteDao();
        FakeAplikimDao aplikimDao = new FakeAplikimDao();
        FakeKandidatDao kandidatDao = new FakeKandidatDao();
        FakeAuditDao auditDao = new FakeAuditDao();
        FakeNjoftimDao njoftimDao = new FakeNjoftimDao();

        InterviewService service = new InterviewService(intervisteDao, aplikimDao, kandidatDao, auditDao, njoftimDao);

        Kandidat k = new Kandidat();
        k.setIdPerdorues(123);
        int idKandidat = kandidatDao.create(k);

        int idAplikimi = aplikimDao.create(new Aplikim() {{
            setIdKandidat(idKandidat);
            setIdPozicion(1);
            setIdCv(1);
            setGjendja("DERGUAR");
        }});

        var res = service.caktoInterviste(idAplikimi, LocalDate.now().plusDays(2), "10:00", "Online", 500);
        assertThat(res).isPresent();

        assertThat(auditDao.findByPerdoruesId(500)).hasSize(1);
        assertThat(njoftimDao.findByPerdoruesId(123)).hasSize(1);
    }

    @Test
    void setResult_success_updates_and_notifies() {
        FakeIntervisteDao intervisteDao = new FakeIntervisteDao();
        FakeAplikimDao aplikimDao = new FakeAplikimDao();
        FakeKandidatDao kandidatDao = new FakeKandidatDao();
        FakeAuditDao auditDao = new FakeAuditDao();
        FakeNjoftimDao njoftimDao = new FakeNjoftimDao();

        InterviewService service = new InterviewService(intervisteDao, aplikimDao, kandidatDao, auditDao, njoftimDao);

        Kandidat k = new Kandidat();
        k.setIdPerdorues(321);
        int idKandidat = kandidatDao.create(k);

        int idAplikimi = aplikimDao.create(new Aplikim() {{
            setIdKandidat(idKandidat);
            setIdPozicion(1);
            setIdCv(1);
            setGjendja("DERGUAR");
        }});

        int idInterviste = intervisteDao.create(new com.example.cvscreening.model.Interviste() {{
            setIdAplikimi(idAplikimi);
            setRezultati("NE_PRITJE");
        }});

        boolean ok = service.vendosRezultatin(idInterviste, "KALON", 9, "Shume mire", 900);
        assertThat(ok).isTrue();

        assertThat(auditDao.findByPerdoruesId(900)).hasSize(1);
        assertThat(njoftimDao.findByPerdoruesId(321)).hasSize(1);
    }
}
