package com.example.cvscreening.service.fake;

import com.example.cvscreening.dao.CvDao;
import com.example.cvscreening.model.Cv;

import java.util.*;

public class FakeCvDao implements CvDao {
    private final Map<Integer, Cv> byId = new HashMap<>();
    private int seq = 1;

    @Override
    public int create(Cv cv) {
        int id = seq++;
        Cv c = cloneCv(cv);
        c.setIdCv(id);
        byId.put(id, c);
        return id;
    }

    @Override
    public Optional<Cv> findById(int idCv) {
        Cv cv = byId.get(idCv);
        return cv == null ? Optional.empty() : Optional.of(cloneCv(cv));
    }

    @Override
    public List<Cv> findByKandidatId(int idKandidat) {
        List<Cv> out = new ArrayList<>();
        for (Cv c : byId.values()) {
            if (Objects.equals(c.getIdKandidat(), idKandidat)) out.add(cloneCv(c));
        }
        out.sort(Comparator.comparing(Cv::getIdCv));
        return out;
    }

    private Cv cloneCv(Cv cv) {
        Cv c = new Cv();
        c.setIdCv(cv.getIdCv());
        c.setIdKandidat(cv.getIdKandidat());
        c.setEmriSkedarit(cv.getEmriSkedarit());
        c.setFormatiSkedarit(cv.getFormatiSkedarit());
        c.setMadhesiaKb(cv.getMadhesiaKb());
        c.setDataNgarkimit(cv.getDataNgarkimit());
        c.setVersion(cv.getVersion());
        c.setPerputhshmeriaMePozicionin(cv.getPerputhshmeriaMePozicionin());
        return c;
    }
    @Override
    public boolean updateMatchScore(int idCv, double score) {
        Cv cv = byId.get(idCv);
        if (cv == null) return false;
        cv.setPerputhshmeriaMePozicionin(score);
        return true;
    }

}
