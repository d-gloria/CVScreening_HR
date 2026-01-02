package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.*;
import com.example.cvscreening.model.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class IntervisteDaoJdbcTest {

    @Test
    void createAndFindByAplikimId() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        KandidatDao kandidatDao = new KandidatDaoJdbc(conn);
        CvDao cvDao = new CvDaoJdbc(conn);
        PozicionPuneDao pozicionDao = new PozicionPuneDaoJdbc(conn);
        AplikimDao aplikimDao = new AplikimDaoJdbc(conn);
        IntervisteDao intervisteDao = new IntervisteDaoJdbc(conn);

        // recruiter
        Perdorues recruiter = new Perdorues();
        recruiter.setEmri("Recruiter");
        recruiter.setMbiemri("One");
        recruiter.setAdresaEmail("recruiter.int@test.com");
        recruiter.setFjalekalimi("secret");
        recruiter.setRoli("REKRUTUES");
        recruiter.setDataRegjistrimit(LocalDate.now());
        recruiter.setGjendja("AKTIV");
        int idRecruiter = perdoruesDao.create(recruiter);

        // candidate user
        Perdorues user = new Perdorues();
        user.setEmri("Gloria");
        user.setMbiemri("Doda");
        user.setAdresaEmail("gloria.int@test.com");
        user.setFjalekalimi("secret");
        user.setRoli("KANDIDAT");
        user.setDataRegjistrimit(LocalDate.now());
        user.setGjendja("AKTIV");
        int idPerdorues = perdoruesDao.create(user);

        Kandidat k = new Kandidat();
        k.setIdPerdorues(idPerdorues);
        int idKandidat = kandidatDao.create(k);

        Cv cv = new Cv();
        cv.setIdKandidat(idKandidat);
        cv.setEmriSkedarit("cv.pdf");
        cv.setFormatiSkedarit("PDF");
        cv.setDataNgarkimit(LocalDate.now());
        int idCv = cvDao.create(cv);

        PozicionPune p = new PozicionPune();
        p.setTitulliPozicionit("Java Developer");
        p.setDepartamenti("IT");
        p.setDataHapjes(LocalDate.now());
        p.setDataAplikimit(LocalDate.now().plusDays(30));
        p.setGjendja("HAPUR");
        p.setKrijuarNga(idRecruiter);
        int idPozicion = pozicionDao.create(p);

        Aplikim a = new Aplikim();
        a.setIdKandidat(idKandidat);
        a.setIdCv(idCv);
        a.setIdPozicion(idPozicion);
        a.setDataAplikimit(LocalDate.now());
        a.setGjendja("DERGUAR");
        int idAplikimi = aplikimDao.create(a);

        Interviste i = new Interviste();
        i.setIdAplikimi(idAplikimi);
        i.setDataIntervistes(LocalDate.now().plusDays(2));
        i.setOra("10:00");
        i.setVendi("Online");
        i.setVleresimiRekrutuesit(8);
        i.setPershtypja("Good communication");
        i.setRezultati("NE_PRITJE");

        int idInterviste = intervisteDao.create(i);

        var byId = intervisteDao.findById(idInterviste);
        assertThat(byId).isPresent();
        assertThat(byId.get().getIdAplikimi()).isEqualTo(idAplikimi);

        var list = intervisteDao.findByAplikimId(idAplikimi);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getIdInterviste()).isEqualTo(idInterviste);

        conn.close();
    }
}
