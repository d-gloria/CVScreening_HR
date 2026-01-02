package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.IntervisteDao;
import com.example.cvscreening.model.Interviste;

import java.util.*;

public class FakeIntervisteDao implements IntervisteDao {

    private final Map<Integer, Interviste> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(Interviste i) {
        int id = seq++;
        Interviste c = cloneI(i);
        c.setIdInterviste(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<Interviste> findById(int idInterviste) {
        Interviste i = byId.get(idInterviste);
        return i == null ? Optional.empty() : Optional.of(cloneI(i));
    }

    @Override
    public List<Interviste> findByAplikimId(int idAplikimi) {
        List<Interviste> out = new ArrayList<>();
        for (Interviste i : byId.values()) {
            if (Objects.equals(i.getIdAplikimi(), idAplikimi)) out.add(cloneI(i));
        }
        out.sort(Comparator.comparing(Interviste::getIdInterviste));
        return out;
    }

    @Override
    public boolean updateRezultat(int idInterviste, String rezultat, Integer vleresimRekrutuesit, String pershtypja) {
        Interviste i = byId.get(idInterviste);
        if (i == null) return false;
        i.setRezultati(rezultat);
        i.setVleresimiRekrutuesit(vleresimRekrutuesit);
        i.setPershtypja(pershtypja);
        return true;
    }

    private Interviste cloneI(Interviste i) {
        Interviste c = new Interviste();
        c.setIdInterviste(i.getIdInterviste());
        c.setIdAplikimi(i.getIdAplikimi());
        c.setDataIntervistes(i.getDataIntervistes());
        c.setOra(i.getOra());
        c.setVendi(i.getVendi());
        c.setVleresimiRekrutuesit(i.getVleresimiRekrutuesit());
        c.setPershtypja(i.getPershtypja());
        c.setRezultati(i.getRezultati());
        return c;
    }
}
