package com.example.cvscreening.dao;

import com.example.cvscreening.model.Perdorues;

import java.util.Optional;

public interface PerdoruesDao {
    int create(Perdorues p);
    Optional<Perdorues> findById(int id);
    Optional<Perdorues> findByEmail(String email);
}
