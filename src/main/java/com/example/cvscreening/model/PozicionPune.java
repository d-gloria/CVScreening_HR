package com.example.cvscreening.model;

import java.time.LocalDate;

public class PozicionPune {
    private Integer idPozicion;
    private String titulliPozicionit;
    private String pershkrimiDetyra;
    private String departamenti;
    private LocalDate dataHapjes;
    private LocalDate dataAplikimit;
    private String kriteret;
    private String gjendja;
    private Integer krijuarNga; // perdorues id (rekrutues/admin)

    public Integer getIdPozicion() { return idPozicion; }
    public void setIdPozicion(Integer idPozicion) { this.idPozicion = idPozicion; }

    public String getTitulliPozicionit() { return titulliPozicionit; }
    public void setTitulliPozicionit(String titulliPozicionit) { this.titulliPozicionit = titulliPozicionit; }

    public String getPershkrimiDetyra() { return pershkrimiDetyra; }
    public void setPershkrimiDetyra(String pershkrimiDetyra) { this.pershkrimiDetyra = pershkrimiDetyra; }

    public String getDepartamenti() { return departamenti; }
    public void setDepartamenti(String departamenti) { this.departamenti = departamenti; }

    public LocalDate getDataHapjes() { return dataHapjes; }
    public void setDataHapjes(LocalDate dataHapjes) { this.dataHapjes = dataHapjes; }

    public LocalDate getDataAplikimit() { return dataAplikimit; }
    public void setDataAplikimit(LocalDate dataAplikimit) { this.dataAplikimit = dataAplikimit; }

    public String getKriteret() { return kriteret; }
    public void setKriteret(String kriteret) { this.kriteret = kriteret; }

    public String getGjendja() { return gjendja; }
    public void setGjendja(String gjendja) { this.gjendja = gjendja; }

    public Integer getKrijuarNga() { return krijuarNga; }
    public void setKrijuarNga(Integer krijuarNga) { this.krijuarNga = krijuarNga; }
}
