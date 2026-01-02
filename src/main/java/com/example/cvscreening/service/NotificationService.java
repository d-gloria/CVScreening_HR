package com.example.cvscreening.service;

import com.example.cvscreening.dao.NjoftimDao;
import com.example.cvscreening.model.Njoftim;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NotificationService {

    private final NjoftimDao njoftimDao;

    public NotificationService(NjoftimDao njoftimDao) {
        this.njoftimDao = njoftimDao;
    }

    // UML: dergoNjoftim(perdoruesId, menyra, permbajtja)
    public Optional<Integer> dergoNjoftim(int idPerdorues, String menyraDergimit, String permbajtja) {
        if (idPerdorues <= 0) return Optional.empty();
        if (permbajtja == null || permbajtja.isBlank()) return Optional.empty();

        Njoftim n = new Njoftim();
        n.setIdPerdorues(idPerdorues);
        n.setMenyraDergimit(menyraDergimit == null || menyraDergimit.isBlank() ? "SISTEM" : menyraDergimit);
        n.setPermbajtja(permbajtja);
        n.setDataDergimit(LocalDate.now());
        n.setStatusiLeximit("PALEXUAR");

        int id = njoftimDao.create(n);
        return Optional.of(id);
    }

    // UML: shenoSiLexuar(njoftimId)
    public boolean shenoSiLexuar(int idNjoftim) {
        if (idNjoftim <= 0) return false;

        // validate exists
        if (njoftimDao.findById(idNjoftim).isEmpty()) return false;

        return njoftimDao.markAsRead(idNjoftim);
    }

    // UML: merrNjoftimePerPerdorues(perdoruesId)
    public List<Njoftim> merrNjoftimePerPerdorues(int idPerdorues) {
        if (idPerdorues <= 0) return List.of();
        return njoftimDao.findByPerdoruesId(idPerdorues);
    }
}
