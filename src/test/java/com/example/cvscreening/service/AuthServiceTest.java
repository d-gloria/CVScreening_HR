package com.example.cvscreening.service;

import com.example.cvscreening.model.Perdorues;
import com.example.cvscreening.service.fake.FakePerdoruesDao;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceTest {

    @Test
    void loginSuccess() {
        FakePerdoruesDao dao = new FakePerdoruesDao();
        AuthService service = new AuthService(dao);

        Perdorues p = new Perdorues();
        p.setEmri("Gloria");
        p.setMbiemri("Doda");
        p.setAdresaEmail("gloria@test.com");
        p.setFjalekalimi("secret123");
        p.setRoli("KANDIDAT");
        p.setGjendja("AKTIV");
        p.setDataRegjistrimit(LocalDate.now());
        dao.create(p);

        var result = service.login("gloria@test.com", "secret123");
        assertThat(result).isPresent();
        assertThat(result.get().getAdresaEmail()).isEqualTo("gloria@test.com");
    }

    @Test
    void loginFailsWrongPassword() {
        FakePerdoruesDao dao = new FakePerdoruesDao();
        AuthService service = new AuthService(dao);

        Perdorues p = new Perdorues();
        p.setAdresaEmail("a@test.com");
        p.setFjalekalimi("correct");
        p.setGjendja("AKTIV");
        dao.create(p);

        var result = service.login("a@test.com", "wrong");
        assertThat(result).isEmpty();
    }

    @Test
    void changePasswordSuccess() {
        FakePerdoruesDao dao = new FakePerdoruesDao();
        AuthService service = new AuthService(dao);

        Perdorues p = new Perdorues();
        p.setAdresaEmail("b@test.com");
        p.setFjalekalimi("oldpass");
        p.setGjendja("AKTIV");
        int id = dao.create(p);

        boolean ok = service.ndryshoFjalekalimin(id, "oldpass", "newpass1");
        assertThat(ok).isTrue();

        var login = service.login("b@test.com", "newpass1");
        assertThat(login).isPresent();
    }
}
