package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.PerdoruesDaoJdbc;
import com.example.cvscreening.dao.jdbc.RaportDaoJdbc;
import com.example.cvscreening.model.Perdorues;
import com.example.cvscreening.model.Raport;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class RaportDaoJdbcTest {

    @Test
    void createAndFindByRekrutuesId() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        RaportDao raportDao = new RaportDaoJdbc(conn);

        Perdorues recruiter = new Perdorues();
        recruiter.setEmri("Recruiter");
        recruiter.setMbiemri("Two");
        recruiter.setAdresaEmail("raport.recr@test.com");
        recruiter.setFjalekalimi("secret");
        recruiter.setRoli("REKRUTUES");
        recruiter.setDataRegjistrimit(LocalDate.now());
        recruiter.setGjendja("AKTIV");
        int idRekrutues = perdoruesDao.create(recruiter);

        Raport r = new Raport();
        r.setIdRekrutues(idRekrutues);
        r.setDataGjenerimit(LocalDate.now());
        r.setLlojiRaportit("KPI");
        r.setPershkrimi("Raport javor");
        r.setFormatiRaportit("PDF");

        int id = raportDao.create(r);

        var byId = raportDao.findById(id);
        assertThat(byId).isPresent();

        var list = raportDao.findByRekrutuesId(idRekrutues);
        assertThat(list).hasSize(1);

        conn.close();
    }
}
