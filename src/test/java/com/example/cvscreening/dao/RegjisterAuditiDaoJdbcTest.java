package com.example.cvscreening.dao;

import com.example.cvscreening.config.Db;
import com.example.cvscreening.dao.jdbc.PerdoruesDaoJdbc;
import com.example.cvscreening.dao.jdbc.RegjisterAuditiDaoJdbc;
import com.example.cvscreening.model.Perdorues;
import com.example.cvscreening.model.RegjisterAuditi;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class RegjisterAuditiDaoJdbcTest {

    @Test
    void createAndFindByPerdoruesId() throws Exception {
        Connection conn = Db.connect();
        Db.runSchema(conn);

        PerdoruesDao perdoruesDao = new PerdoruesDaoJdbc(conn);
        RegjisterAuditiDao auditDao = new RegjisterAuditiDaoJdbc(conn);

        Perdorues p = new Perdorues();
        p.setEmri("User");
        p.setMbiemri("Audit");
        p.setAdresaEmail("audit.user@test.com");
        p.setFjalekalimi("secret");
        p.setRoli("KANDIDAT");
        p.setDataRegjistrimit(LocalDate.now());
        p.setGjendja("AKTIV");
        int idPerdorues = perdoruesDao.create(p);

        RegjisterAuditi a = new RegjisterAuditi();
        a.setIdPerdorues(idPerdorues);
        a.setObjekti("CV");
        a.setVeprimi("CREATE");
        a.setDataVeprimit(LocalDate.now());
        a.setOraVeprimit("12:00");
        a.setPershkrimiDetajuar("Ngarkim CV");

        int id = auditDao.create(a);

        var byId = auditDao.findById(id);
        assertThat(byId).isPresent();

        var list = auditDao.findByPerdoruesId(idPerdorues);
        assertThat(list).hasSize(1);

        conn.close();
    }
}
