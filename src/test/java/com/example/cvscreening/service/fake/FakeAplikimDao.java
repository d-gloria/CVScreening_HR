package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.AplikimDao;
import com.example.cvscreening.model.Aplikim;

import java.util.*;

public class FakeAplikimDao implements AplikimDao {
    private final Map<Integer, Aplikim> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(Aplikim a) {
        int id = seq++;
        Aplikim c = cloneA(a);
        c.setIdAplikimi(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<Aplikim> findById(int idAplikimi) {
        Aplikim a = byId.get(idAplikimi);
        return a == null ? Optional.empty() : Optional.of(cloneA(a));
    }

    @Override
    public List<Aplikim> findByKandidatId(int idKandidat) {
        List<Aplikim> out = new ArrayList<>();
        for (Aplikim a : byId.values()) {
            if (Objects.equals(a.getIdKandidat(), idKandidat)) out.add(cloneA(a));
        }
        out.sort(Comparator.comparing(Aplikim::getIdAplikimi));
        return out;
    }

    @Override
    public List<Aplikim> findByPozicionId(int idPozicion) {
        List<Aplikim> out = new ArrayList<>();
        for (Aplikim a : byId.values()) {
            if (Objects.equals(a.getIdPozicion(), idPozicion)) out.add(cloneA(a));
        }
        out.sort(Comparator.comparing(Aplikim::getIdAplikimi));
        return out;
    }

    @Override
    public boolean updateStatusAndComment(int idAplikimi, String gjendja, String komentetRekrutuesit) {
        Aplikim a = byId.get(idAplikimi);
        if (a == null) return false;
        a.setGjendja(gjendja);
        a.setKomentetRekrutuesit(komentetRekrutuesit);
        return true;
    }

    private Aplikim cloneA(Aplikim a) {
        Aplikim c = new Aplikim();
        c.setIdAplikimi(a.getIdAplikimi());
        c.setIdKandidat(a.getIdKandidat());
        c.setIdPozicion(a.getIdPozicion());
        c.setIdCv(a.getIdCv());
        c.setDataAplikimit(a.getDataAplikimit());
        c.setVleresimi(a.getVleresimi());
        c.setGjendja(a.getGjendja());
        c.setKomentetRekrutuesit(a.getKomentetRekrutuesit());
        return c;
    }

    @Override
    public boolean updateScore(int idAplikimi, double score) {
        Aplikim a = byId.get(idAplikimi);
        if (a == null) return false;
        a.setVleresimi(score);
        return true;
    }

}
