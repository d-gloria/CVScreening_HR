package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.PerdoruesDao;
import com.example.cvscreening.model.Perdorues;

import java.util.*;

public class FakePerdoruesDao implements PerdoruesDao {

    private final Map<Integer, Perdorues> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(Perdorues p) {
        int id = seq++;
        Perdorues copy = cloneUser(p);
        copy.setIdPerdorues(id);
        byId.put(id, copy);
        return id;
    }

    @Override
    public Optional<Perdorues> findById(int id) {
        Perdorues p = byId.get(id);
        return p == null ? Optional.empty() : Optional.of(cloneUser(p));
    }

    @Override
    public Optional<Perdorues> findByEmail(String email) {
        return byId.values().stream()
                .filter(u -> Objects.equals(u.getAdresaEmail(), email))
                .findFirst()
                .map(this::cloneUser);
    }

    @Override
    public boolean updatePassword(int idPerdorues, String newPassword) {
        Perdorues u = byId.get(idPerdorues);
        if (u == null) return false;
        u.setFjalekalimi(newPassword);
        return true;
    }

    // --- You may have more methods on PerdoruesDao; implement minimally ---
    // If your interface has other methods you don't use in AuthService tests,
    // you can throw UnsupportedOperationException for them.

    private Perdorues cloneUser(Perdorues p) {
        Perdorues c = new Perdorues();
        c.setIdPerdorues(p.getIdPerdorues());
        c.setEmri(p.getEmri());
        c.setMbiemri(p.getMbiemri());
        c.setAdresaEmail(p.getAdresaEmail());
        c.setFjalekalimi(p.getFjalekalimi());
        c.setRoli(p.getRoli());
        c.setDataRegjistrimit(p.getDataRegjistrimit());
        c.setGjendja(p.getGjendja());
        return c;
    }
}
