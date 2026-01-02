package com.example.cvscreening.model;

import java.time.LocalDate;

public class Aplikim {
    private Integer idAplikimi;
    private Integer idKandidat;
    private Integer idPozicion;
    private Integer idCv;

    private LocalDate dataAplikimit;
    private Double vleresimi;
    private String gjendja;
    private String komentetRekrutuesit;

    public Integer getIdAplikimi() { return idAplikimi; }
    public void setIdAplikimi(Integer idAplikimi) { this.idAplikimi = idAplikimi; }

    public Integer getIdKandidat() { return idKandidat; }
    public void setIdKandidat(Integer idKandidat) { this.idKandidat = idKandidat; }

    public Integer getIdPozicion() { return idPozicion; }
    public void setIdPozicion(Integer idPozicion) { this.idPozicion = idPozicion; }

    public Integer getIdCv() { return idCv; }
    public void setIdCv(Integer idCv) { this.idCv = idCv; }

    public LocalDate getDataAplikimit() { return dataAplikimit; }
    public void setDataAplikimit(LocalDate dataAplikimit) { this.dataAplikimit = dataAplikimit; }

    public Double getVleresimi() { return vleresimi; }
    public void setVleresimi(Double vleresimi) { this.vleresimi = vleresimi; }

    public String getGjendja() { return gjendja; }
    public void setGjendja(String gjendja) { this.gjendja = gjendja; }

    public String getKomentetRekrutuesit() { return komentetRekrutuesit; }
    public void setKomentetRekrutuesit(String komentetRekrutuesit) { this.komentetRekrutuesit = komentetRekrutuesit; }
}
