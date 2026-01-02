package com.example.cvscreening.dao;

import com.example.cvscreening.model.Interviste;

import java.util.List;
import java.util.Optional;

public interface IntervisteDao {
    int create(Interviste i);

    Optional<Interviste> findById(int idInterviste);

    List<Interviste> findByAplikimId(int idAplikimi);

    boolean updateRezultat(int idInterviste, String rezultat, Integer vleresimRekrutuesit, String pershtypja);
}
