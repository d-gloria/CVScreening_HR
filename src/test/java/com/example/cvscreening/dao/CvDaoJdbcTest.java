package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.CvDaoJdbc;
import com.example.cvscreening.dao.jdbc.KandidatDaoJdbc;
import com.example.cvscreening.dao.jdbc.PerdoruesDaoJdbc;
import com.example.cvscreening.model.Cv;
import com.example.cvscreening.model.Kandidat;
import com.example.cvscreening.model.Perdorues;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CvDaoJdbcTest {

    @Test
    void createAndFindByKandidatId() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        KandidatDao kandidatDao = new KandidatDaoJdbc(conn);
        CvDao cvDao = new CvDaoJdbc(conn);

        // 1) Create Perdorues
        Perdorues p = new Perdorues();
        p.setEmri("Gloria");
        p.setMbiemri("Doda");
        p.setAdresaEmail("gloria.cv@test.com");
        p.setFjalekalimi("secret");
        p.setRoli("KANDIDAT");
        p.setDataRegjistrimit(LocalDate.now());
        p.setGjendja("AKTIV");
        int idPerdorues = perdoruesDao.create(p);

        // 2) Create Kandidat
        Kandidat k = new Kandidat();
        k.setIdPerdorues(idPerdorues);
        k.setDataLindjes(LocalDate.of(2000, 1, 1));
        k.setGjinia("F");
        k.setVendbanimi("Tirane");
        k.setNumriCelular("0690000000");
        k.setNiveliArsimor("Bachelor");
        k.setFushaStudimit("Computer Engineering");
        k.setViteEksperience(1);
        k.setGjuhetEHuaja("EN");
        k.setAftesiteProfesionale("Java, SQL");
        int idKandidat = kandidatDao.create(k);

        // 3) Create CV
        Cv cv1 = new Cv();
        cv1.setIdKandidat(idKandidat);
        cv1.setEmriSkedarit("cv_gloria_v1.pdf");
        cv1.setFormatiSkedarit("PDF");
        cv1.setMadhesiaKb(120);
        cv1.setDataNgarkimit(LocalDate.now());
        cv1.setVersion("v1");
        cv1.setPerputhshmeriaMePozicionin(0.85);

        int idCv1 = cvDao.create(cv1);

        // 4) Verify findById
        var byId = cvDao.findById(idCv1);
        assertThat(byId).isPresent();
        assertThat(byId.get().getEmriSkedarit()).isEqualTo("cv_gloria_v1.pdf");
        assertThat(byId.get().getIdKandidat()).isEqualTo(idKandidat);

        // 5) Verify findByKandidatId
        var list = cvDao.findByKandidatId(idKandidat);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getIdCv()).isEqualTo(idCv1);

        conn.close();
    }
}
