package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.PerdoruesDaoJdbc;
import com.example.cvscreening.dao.jdbc.PozicionPuneDaoJdbc;
import com.example.cvscreening.model.Perdorues;
import com.example.cvscreening.model.PozicionPune;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PozicionPuneDaoJdbcTest {

    @Test
    void createAndFindAll() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        PozicionPuneDao pozicionDao = new PozicionPuneDaoJdbc(conn);

        // create recruiter/admin user (krijuar_nga FK)
        Perdorues recruiter = new Perdorues();
        recruiter.setEmri("Recruiter");
        recruiter.setMbiemri("One");
        recruiter.setAdresaEmail("recruiter@test.com");
        recruiter.setFjalekalimi("secret");
        recruiter.setRoli("REKRUTUES");
        recruiter.setDataRegjistrimit(LocalDate.now());
        recruiter.setGjendja("AKTIV");
        int idRecruiter = perdoruesDao.create(recruiter);

        PozicionPune p = new PozicionPune();
        p.setTitulliPozicionit("Java Developer");
        p.setPershkrimiDetyra("Backend development");
        p.setDepartamenti("IT");
        p.setDataHapjes(LocalDate.now());
        p.setDataAplikimit(LocalDate.now().plusDays(30));
        p.setKriteret("Java, SQL");
        p.setGjendja("HAPUR");
        p.setKrijuarNga(idRecruiter);

        int idPozicion = pozicionDao.create(p);

        var byId = pozicionDao.findById(idPozicion);
        assertThat(byId).isPresent();
        assertThat(byId.get().getTitulliPozicionit()).isEqualTo("Java Developer");

        var all = pozicionDao.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all.get(0).getIdPozicion()).isEqualTo(idPozicion);

        conn.close();
    }
}
