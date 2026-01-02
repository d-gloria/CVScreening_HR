package com.example.cvscreening.service;

import com.example.cvscreening.dao.PerdoruesDao;
import com.example.cvscreening.model.Perdorues;

import java.util.Optional;

public class AuthService {

    private final PerdoruesDao perdoruesDao;

    public AuthService(PerdoruesDao perdoruesDao) {
        this.perdoruesDao = perdoruesDao;
    }

    // UML: login(email, password)
    public Optional<Perdorues> login(String email, String fjalekalimi) {
        if (email == null || email.isBlank()) return Optional.empty();
        if (fjalekalimi == null) return Optional.empty();

        Optional<Perdorues> userOpt = perdoruesDao.findByEmail(email);
        if (userOpt.isEmpty()) return Optional.empty();

        Perdorues u = userOpt.get();

        // Optional: block inactive users
        if (u.getGjendja() != null && u.getGjendja().equalsIgnoreCase("JOAKTIV")) return Optional.empty();

        if (!fjalekalimi.equals(u.getFjalekalimi())) return Optional.empty();
        return Optional.of(u);
    }

    // UML: ndryshoFjalekalimin(userId, old, new)
    public boolean ndryshoFjalekalimin(int idPerdorues, String oldPass, String newPass) {
        if (newPass == null || newPass.length() < 6) return false;

        Optional<Perdorues> userOpt = perdoruesDao.findById(idPerdorues);
        if (userOpt.isEmpty()) return false;

        Perdorues u = userOpt.get();
        if (!oldPass.equals(u.getFjalekalimi())) return false;

        u.setFjalekalimi(newPass);
        return perdoruesDao.updatePassword(idPerdorues, newPass);
    }
}
