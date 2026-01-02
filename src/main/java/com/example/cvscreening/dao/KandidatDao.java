package com.example.cvscreening.dao;

import com.example.cvscreening.model.Kandidat;

import java.util.Optional;

public interface KandidatDao {
    int create(Kandidat k);

    Optional<Kandidat> findById(int idKandidat);

    Optional<Kandidat> findByPerdoruesId(int idPerdorues);
}
