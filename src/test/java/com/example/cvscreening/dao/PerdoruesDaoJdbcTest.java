package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.PerdoruesDao;
import com.example.cvscreening.dao.jdbc.PerdoruesDaoJdbc;
import com.example.cvscreening.model.Perdorues;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PerdoruesDaoJdbcTest {

    @Test
    void createAndFind() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao dao = new PerdoruesDaoJdbc(conn);

        Perdorues p = new Perdorues();
        p.setEmri("Gloria");
        p.setMbiemri("Doda");
        p.setAdresaEmail("gloria@test.com");
        p.setFjalekalimi("secret");
        p.setRoli("KANDIDAT");
        p.setDataRegjistrimit(LocalDate.now());
        p.setGjendja("AKTIV");

        int id = dao.create(p);

        var found = dao.findById(id);
        assertThat(found).isPresent();
        assertThat(found.get().getAdresaEmail()).isEqualTo("gloria@test.com");

        conn.close();
    }
}
