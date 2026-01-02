package com.example.cvscreening.dao;

import com.example.cvscreening.model.Aplikim;

import java.util.List;
import java.util.Optional;

public interface AplikimDao {
    int create(Aplikim a);

    Optional<Aplikim> findById(int idAplikimi);

    List<Aplikim> findByKandidatId(int idKandidat);

    List<Aplikim> findByPozicionId(int idPozicion);
}
