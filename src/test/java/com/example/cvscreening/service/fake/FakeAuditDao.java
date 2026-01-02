package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.RegjisterAuditiDao;
import com.example.cvscreening.model.RegjisterAuditi;

import java.util.*;

public class FakeAuditDao implements RegjisterAuditiDao {
    private final Map<Integer, RegjisterAuditi> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(RegjisterAuditi a) {
        int id = seq++;
        RegjisterAuditi c = cloneA(a);
        c.setIdVeprimi(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<RegjisterAuditi> findById(int idVeprimi) {
        RegjisterAuditi a = byId.get(idVeprimi);
        return a == null ? Optional.empty() : Optional.of(cloneA(a));
    }

    @Override
    public List<RegjisterAuditi> findByPerdoruesId(int idPerdorues) {
        List<RegjisterAuditi> out = new ArrayList<>();
        for (RegjisterAuditi a : byId.values()) {
            if (a.getIdPerdorues() != null && a.getIdPerdorues() == idPerdorues) out.add(cloneA(a));
        }
        out.sort(Comparator.comparing(RegjisterAuditi::getIdVeprimi));
        return out;
    }

    private RegjisterAuditi cloneA(RegjisterAuditi a) {
        RegjisterAuditi c = new RegjisterAuditi();
        c.setIdVeprimi(a.getIdVeprimi());
        c.setIdPerdorues(a.getIdPerdorues());
        c.setObjekti(a.getObjekti());
        c.setVeprimi(a.getVeprimi());
        c.setDataVeprimit(a.getDataVeprimit());
        c.setOraVeprimit(a.getOraVeprimit());
        c.setPershkrimiDetajuar(a.getPershkrimiDetajuar());
        return c;
    }
}
