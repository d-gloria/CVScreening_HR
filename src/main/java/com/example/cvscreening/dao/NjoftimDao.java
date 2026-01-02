package com.example.cvscreening.dao;

import com.example.cvscreening.model.Njoftim;

import java.util.List;
import java.util.Optional;

public interface NjoftimDao {
    int create(Njoftim n);
    Optional<Njoftim> findById(int idNjoftim);
    List<Njoftim> findByPerdoruesId(int idPerdorues);
}
