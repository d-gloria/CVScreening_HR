package com.example.cvscreening.model;

import java.time.LocalDate;

public class Interviste {
    private Integer idInterviste;
    private Integer idAplikimi;

    private LocalDate dataIntervistes;
    private String ora;
    private String vendi;
    private Integer vleresimiRekrutuesit;
    private String pershtypja;
    private String rezultati;

    public Integer getIdInterviste() { return idInterviste; }
    public void setIdInterviste(Integer idInterviste) { this.idInterviste = idInterviste; }

    public Integer getIdAplikimi() { return idAplikimi; }
    public void setIdAplikimi(Integer idAplikimi) { this.idAplikimi = idAplikimi; }

    public LocalDate getDataIntervistes() { return dataIntervistes; }
    public void setDataIntervistes(LocalDate dataIntervistes) { this.dataIntervistes = dataIntervistes; }

    public String getOra() { return ora; }
    public void setOra(String ora) { this.ora = ora; }

    public String getVendi() { return vendi; }
    public void setVendi(String vendi) { this.vendi = vendi; }

    public Integer getVleresimiRekrutuesit() { return vleresimiRekrutuesit; }
    public void setVleresimiRekrutuesit(Integer vleresimiRekrutuesit) { this.vleresimiRekrutuesit = vleresimiRekrutuesit; }

    public String getPershtypja() { return pershtypja; }
    public void setPershtypja(String pershtypja) { this.pershtypja = pershtypja; }

    public String getRezultati() { return rezultati; }
    public void setRezultati(String rezultati) { this.rezultati = rezultati; }
}
