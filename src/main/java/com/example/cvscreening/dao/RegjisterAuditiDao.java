package com.example.cvscreening.dao;

import com.example.cvscreening.model.RegjisterAuditi;

import java.util.List;
import java.util.Optional;

public interface RegjisterAuditiDao {
    int create(RegjisterAuditi a);
    Optional<RegjisterAuditi> findById(int idVeprimi);
    List<RegjisterAuditi> findByPerdoruesId(int idPerdorues);
}
