package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.PozicionPuneDao;
import com.example.cvscreening.model.PozicionPune;

import java.util.*;

public class FakePozicionPuneDao implements PozicionPuneDao {
    private final Map<Integer, PozicionPune> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(PozicionPune p) {
        int id = seq++;
        PozicionPune c = cloneP(p);
        c.setIdPozicion(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<PozicionPune> findById(int idPozicion) {
        PozicionPune p = byId.get(idPozicion);
        return p == null ? Optional.empty() : Optional.of(cloneP(p));
    }

    @Override
    public List<PozicionPune> findAll() {
        List<PozicionPune> out = new ArrayList<>();
        for (PozicionPune p : byId.values()) out.add(cloneP(p));
        out.sort(Comparator.comparing(PozicionPune::getIdPozicion));
        return out;
    }

    private PozicionPune cloneP(PozicionPune p) {
        PozicionPune c = new PozicionPune();
        c.setIdPozicion(p.getIdPozicion());
        c.setTitulliPozicionit(p.getTitulliPozicionit());
        c.setPershkrimiDetyra(p.getPershkrimiDetyra());
        c.setDepartamenti(p.getDepartamenti());
        c.setDataHapjes(p.getDataHapjes());
        c.setDataAplikimit(p.getDataAplikimit());
        c.setKriteret(p.getKriteret());
        c.setGjendja(p.getGjendja());
        c.setKrijuarNga(p.getKrijuarNga());
        return c;
    }
}
