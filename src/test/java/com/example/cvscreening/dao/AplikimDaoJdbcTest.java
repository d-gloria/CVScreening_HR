package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.*;
import com.example.cvscreening.model.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AplikimDaoJdbcTest {

    @Test
    void createAndFindByKandidatAndPozicion() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        KandidatDao kandidatDao = new KandidatDaoJdbc(conn);
        CvDao cvDao = new CvDaoJdbc(conn);
        PozicionPuneDao pozicionDao = new PozicionPuneDaoJdbc(conn);
        AplikimDao aplikimDao = new AplikimDaoJdbc(conn);

        // recruiter (creates job)
        Perdorues recruiter = new Perdorues();
        recruiter.setEmri("Recruiter");
        recruiter.setMbiemri("One");
        recruiter.setAdresaEmail("recruiter.apl@test.com");
        recruiter.setFjalekalimi("secret");
        recruiter.setRoli("REKRUTUES");
        recruiter.setDataRegjistrimit(LocalDate.now());
        recruiter.setGjendja("AKTIV");
        int idRecruiter = perdoruesDao.create(recruiter);

        // candidate user
        Perdorues user = new Perdorues();
        user.setEmri("Gloria");
        user.setMbiemri("Doda");
        user.setAdresaEmail("gloria.apl@test.com");
        user.setFjalekalimi("secret");
        user.setRoli("KANDIDAT");
        user.setDataRegjistrimit(LocalDate.now());
        user.setGjendja("AKTIV");
        int idPerdorues = perdoruesDao.create(user);

        Kandidat k = new Kandidat();
        k.setIdPerdorues(idPerdorues);
        k.setDataLindjes(LocalDate.of(2000, 1, 1));
        k.setVendbanimi("Tirane");
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
        a.setVleresimi(0.75);
        a.setKomentetRekrutuesit("OK");

        int idAplikimi = aplikimDao.create(a);

        var byId = aplikimDao.findById(idAplikimi);
        assertThat(byId).isPresent();
        assertThat(byId.get().getIdPozicion()).isEqualTo(idPozicion);

        var byKandidat = aplikimDao.findByKandidatId(idKandidat);
        assertThat(byKandidat).hasSize(1);

        var byPozicion = aplikimDao.findByPozicionId(idPozicion);
        assertThat(byPozicion).hasSize(1);

        conn.close();
    }
}
