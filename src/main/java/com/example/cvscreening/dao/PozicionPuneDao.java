package com.example.cvscreening.dao;

import com.example.cvscreening.model.PozicionPune;

import java.util.List;
import java.util.Optional;

public interface PozicionPuneDao {
    int create(PozicionPune p);

    Optional<PozicionPune> findById(int idPozicion);

    List<PozicionPune> findAll();
}
