package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.KandidatDaoJdbc;
import com.example.cvscreening.dao.jdbc.PerdoruesDaoJdbc;
import com.example.cvscreening.model.Kandidat;
import com.example.cvscreening.model.Perdorues;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class KandidatDaoJdbcTest {

    @Test
    void createAndFindByPerdoruesId() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        KandidatDao kandidatDao = new KandidatDaoJdbc(conn);

        // Create parent perdorues first (FK requirement)
        Perdorues p = new Perdorues();
        p.setEmri("Gloria");
        p.setMbiemri("Doda");
        p.setAdresaEmail("gloria.k@test.com");
        p.setFjalekalimi("secret");
        p.setRoli("KANDIDAT");
        p.setDataRegjistrimit(LocalDate.now());
        p.setGjendja("AKTIV");

        int idPerdorues = perdoruesDao.create(p);

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

        var foundById = kandidatDao.findById(idKandidat);
        assertThat(foundById).isPresent();
        assertThat(foundById.get().getIdPerdorues()).isEqualTo(idPerdorues);

        var foundByPerdorues = kandidatDao.findByPerdoruesId(idPerdorues);
        assertThat(foundByPerdorues).isPresent();
        assertThat(foundByPerdorues.get().getIdKandidat()).isEqualTo(idKandidat);

        conn.close();
    }
}
