package com.example.cvscreening.model;

import java.time.LocalDate;

public class Kandidat {
    private Integer idKandidat;
    private Integer idPerdorues;

    private LocalDate dataLindjes;
    private String gjinia;
    private String vendbanimi;
    private String numriCelular;

    private String niveliArsimor;
    private String fushaStudimit;
    private Integer viteEksperience;

    private String gjuhetEHuaja;
    private String aftesiteProfesionale;

    public Integer getIdKandidat() { return idKandidat; }
    public void setIdKandidat(Integer idKandidat) { this.idKandidat = idKandidat; }

    public Integer getIdPerdorues() { return idPerdorues; }
    public void setIdPerdorues(Integer idPerdorues) { this.idPerdorues = idPerdorues; }

    public LocalDate getDataLindjes() { return dataLindjes; }
    public void setDataLindjes(LocalDate dataLindjes) { this.dataLindjes = dataLindjes; }

    public String getGjinia() { return gjinia; }
    public void setGjinia(String gjinia) { this.gjinia = gjinia; }

    public String getVendbanimi() { return vendbanimi; }
    public void setVendbanimi(String vendbanimi) { this.vendbanimi = vendbanimi; }

    public String getNumriCelular() { return numriCelular; }
    public void setNumriCelular(String numriCelular) { this.numriCelular = numriCelular; }

    public String getNiveliArsimor() { return niveliArsimor; }
    public void setNiveliArsimor(String niveliArsimor) { this.niveliArsimor = niveliArsimor; }

    public String getFushaStudimit() { return fushaStudimit; }
    public void setFushaStudimit(String fushaStudimit) { this.fushaStudimit = fushaStudimit; }

    public Integer getViteEksperience() { return viteEksperience; }
    public void setViteEksperience(Integer viteEksperience) { this.viteEksperience = viteEksperience; }

    public String getGjuhetEHuaja() { return gjuhetEHuaja; }
    public void setGjuhetEHuaja(String gjuhetEHuaja) { this.gjuhetEHuaja = gjuhetEHuaja; }

    public String getAftesiteProfesionale() { return aftesiteProfesionale; }
    public void setAftesiteProfesionale(String aftesiteProfesionale) { this.aftesiteProfesionale = aftesiteProfesionale; }
}
