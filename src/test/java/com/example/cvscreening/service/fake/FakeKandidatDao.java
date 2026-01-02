package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.KandidatDao;
import com.example.cvscreening.model.Kandidat;

import java.util.*;

public class FakeKandidatDao implements KandidatDao {
    private final Map<Integer, Kandidat> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(Kandidat k) {
        int id = seq++;
        Kandidat c = cloneK(k);
        c.setIdKandidat(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<Kandidat> findById(int idKandidat) {
        Kandidat k = byId.get(idKandidat);
        return k == null ? Optional.empty() : Optional.of(cloneK(k));
    }

    @Override
    public Optional<Kandidat> findByPerdoruesId(int idPerdorues) {
        return byId.values().stream()
                .filter(k -> Objects.equals(k.getIdPerdorues(), idPerdorues))
                .findFirst()
                .map(this::cloneK);
    }

    private Kandidat cloneK(Kandidat k) {
        Kandidat c = new Kandidat();
        c.setIdKandidat(k.getIdKandidat());
        c.setIdPerdorues(k.getIdPerdorues());
        c.setDataLindjes(k.getDataLindjes());
        c.setGjinia(k.getGjinia());
        c.setVendbanimi(k.getVendbanimi());
        c.setNumriCelular(k.getNumriCelular());
        c.setNiveliArsimor(k.getNiveliArsimor());
        c.setFushaStudimit(k.getFushaStudimit());
        c.setViteEksperience(k.getViteEksperience());
        c.setGjuhetEHuaja(k.getGjuhetEHuaja());
        c.setAftesiteProfesionale(k.getAftesiteProfesionale());
        return c;
    }
}
