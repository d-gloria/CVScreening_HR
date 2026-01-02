package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.RaportDao;
import com.example.cvscreening.model.Raport;

import java.util.*;

public class FakeRaportDao implements RaportDao {

    private final Map<Integer, Raport> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(Raport r) {
        int id = seq++;
        Raport c = cloneR(r);
        c.setIdRaport(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<Raport> findById(int idRaport) {
        Raport r = byId.get(idRaport);
        return r == null ? Optional.empty() : Optional.of(cloneR(r));
    }

    @Override
    public List<Raport> findByRekrutuesId(int idRekrutues) {
        List<Raport> out = new ArrayList<>();
        for (Raport r : byId.values()) {
            if (Objects.equals(r.getIdRekrutues(), idRekrutues)) out.add(cloneR(r));
        }
        out.sort(Comparator.comparing(Raport::getIdRaport));
        return out;
    }

    private Raport cloneR(Raport r) {
        Raport c = new Raport();
        c.setIdRaport(r.getIdRaport());
        c.setIdRekrutues(r.getIdRekrutues());
        c.setDataGjenerimit(r.getDataGjenerimit());
        c.setLlojiRaportit(r.getLlojiRaportit());
        c.setPershkrimi(r.getPershkrimi());
        c.setFormatiRaportit(r.getFormatiRaportit());
        return c;
    }
}
