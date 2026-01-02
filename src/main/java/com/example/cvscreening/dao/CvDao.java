package com.example.cvscreening.dao;

import com.example.cvscreening.model.Cv;

import java.util.List;
import java.util.Optional;

public interface CvDao {
    int create(Cv cv);

    Optional<Cv> findById(int idCv);

    List<Cv> findByKandidatId(int idKandidat);
    boolean updateMatchScore(int idCv, double score);

}