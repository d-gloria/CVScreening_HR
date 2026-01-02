package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.NjoftimDao;
import com.example.cvscreening.model.Njoftim;

import java.util.*;

public class FakeNjoftimDao implements NjoftimDao {
    private final Map<Integer, Njoftim> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(Njoftim n) {
        int id = seq++;
        Njoftim c = cloneN(n);
        c.setIdNjoftim(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<Njoftim> findById(int idNjoftim) {
        Njoftim n = byId.get(idNjoftim);
        return n == null ? Optional.empty() : Optional.of(cloneN(n));
    }

    @Override
    public List<Njoftim> findByPerdoruesId(int idPerdorues) {
        List<Njoftim> out = new ArrayList<>();
        for (Njoftim n : byId.values()) {
            if (Objects.equals(n.getIdPerdorues(), idPerdorues)) out.add(cloneN(n));
        }
        out.sort(Comparator.comparing(Njoftim::getIdNjoftim));
        return out;
    }

    private Njoftim cloneN(Njoftim n) {
        Njoftim c = new Njoftim();
        c.setIdNjoftim(n.getIdNjoftim());
        c.setIdPerdorues(n.getIdPerdorues());
        c.setMenyraDergimit(n.getMenyraDergimit());
        c.setPermbajtja(n.getPermbajtja());
        c.setDataDergimit(n.getDataDergimit());
        c.setStatusiLeximit(n.getStatusiLeximit());
        return c;
    }
}
