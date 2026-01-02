package com.example.cvscreening.dao;

import com.example.cvscreening.model.Raport;

import java.util.List;
import java.util.Optional;

public interface RaportDao {
    int create(Raport r);
    Optional<Raport> findById(int idRaport);
    List<Raport> findByRekrutuesId(int idRekrutues);
}
