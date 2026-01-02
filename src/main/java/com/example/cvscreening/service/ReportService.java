package com.example.cvscreening.service;

import com.example.cvscreening.dao.RaportDao;
import com.example.cvscreening.model.Raport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReportService {

    private final RaportDao raportDao;

    public ReportService(RaportDao raportDao) {
        this.raportDao = raportDao;
    }

    // UML: gjeneroRaport(rekrutuesId, lloji, pershkrimi, formati)
    public Optional<Integer> gjeneroRaport(int idRekrutues, String llojiRaportit, String pershkrimi, String formatiRaportit) {
        if (idRekrutues <= 0) return Optional.empty();
        if (llojiRaportit == null || llojiRaportit.isBlank()) return Optional.empty();

        Raport r = new Raport();
        r.setIdRekrutues(idRekrutues);
        r.setDataGjenerimit(LocalDate.now());
        r.setLlojiRaportit(llojiRaportit);
        r.setPershkrimi(pershkrimi);
        r.setFormatiRaportit(formatiRaportit == null || formatiRaportit.isBlank() ? "PDF" : formatiRaportit);

        int id = raportDao.create(r);
        return Optional.of(id);
    }

    // UML: merrRaporte(rekrutuesId)
    public List<Raport> merrRaporte(int idRekrutues) {
        if (idRekrutues <= 0) return List.of();
        return raportDao.findByRekrutuesId(idRekrutues);
    }

    // UML: merrRaport(idRaport)
    public Optional<Raport> merrRaport(int idRaport) {
        if (idRaport <= 0) return Optional.empty();
        return raportDao.findById(idRaport);
    }
}
