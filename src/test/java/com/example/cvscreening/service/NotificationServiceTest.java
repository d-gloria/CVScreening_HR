package com.example.cvscreening.service;

import com.example.cvscreening.service.fake.FakeNjoftimDao;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceTest {

    @Test
    void sendNotification_success() {
        FakeNjoftimDao dao = new FakeNjoftimDao();
        NotificationService service = new NotificationService(dao);

        var idOpt = service.dergoNjoftim(10, "SISTEM", "Test njoftim");
        assertThat(idOpt).isPresent();

        var list = service.merrNjoftimePerPerdorues(10);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getStatusiLeximit()).isEqualTo("PALEXUAR");
    }

    @Test
    void markAsRead_success() {
        FakeNjoftimDao dao = new FakeNjoftimDao();
        NotificationService service = new NotificationService(dao);

        int id = service.dergoNjoftim(11, "SISTEM", "Lexo kete").orElseThrow();
        boolean ok = service.shenoSiLexuar(id);

        assertThat(ok).isTrue();
        var updated = dao.findById(id).orElseThrow();
        assertThat(updated.getStatusiLeximit()).isEqualTo("LEXUAR");
    }

    @Test
    void markAsRead_fails_whenMissing() {
        FakeNjoftimDao dao = new FakeNjoftimDao();
        NotificationService service = new NotificationService(dao);

        boolean ok = service.shenoSiLexuar(999);
        assertThat(ok).isFalse();
    }
}
