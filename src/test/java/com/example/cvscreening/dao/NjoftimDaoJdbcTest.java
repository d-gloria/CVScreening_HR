package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.NjoftimDaoJdbc;
import com.example.cvscreening.dao.jdbc.PerdoruesDaoJdbc;
import com.example.cvscreening.model.Njoftim;
import com.example.cvscreening.model.Perdorues;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class NjoftimDaoJdbcTest {

    @Test
    void createAndFindByPerdoruesId() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        NjoftimDao njoftimDao = new NjoftimDaoJdbc(conn);

        Perdorues p = new Perdorues();
        p.setEmri("User");
        p.setMbiemri("One");
        p.setAdresaEmail("njoftim.user@test.com");
        p.setFjalekalimi("secret");
        p.setRoli("KANDIDAT");
        p.setDataRegjistrimit(LocalDate.now());
        p.setGjendja("AKTIV");
        int idPerdorues = perdoruesDao.create(p);

        Njoftim n = new Njoftim();
        n.setIdPerdorues(idPerdorues);
        n.setMenyraDergimit("EMAIL");
        n.setPermbajtja("Pershendetje!");
        n.setDataDergimit(LocalDate.now());
        n.setStatusiLeximit("PALEXUAR");

        int id = njoftimDao.create(n);

        var byId = njoftimDao.findById(id);
        assertThat(byId).isPresent();

        var list = njoftimDao.findByPerdoruesId(idPerdorues);
        assertThat(list).hasSize(1);

        conn.close();
    }
}
