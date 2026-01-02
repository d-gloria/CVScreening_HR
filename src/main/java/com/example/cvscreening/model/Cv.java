package com.example.cvscreening.model;

import java.time.LocalDate;

public class Cv {
    private Integer idCv;
    private Integer idKandidat;

    private String emriSkedarit;
    private String formatiSkedarit;
    private Integer madhesiaKb;
    private LocalDate dataNgarkimit;
    private String version;
    private Double perputhshmeriaMePozicionin;

    public Integer getIdCv() { return idCv; }
    public void setIdCv(Integer idCv) { this.idCv = idCv; }

    public Integer getIdKandidat() { return idKandidat; }
    public void setIdKandidat(Integer idKandidat) { this.idKandidat = idKandidat; }

    public String getEmriSkedarit() { return emriSkedarit; }
    public void setEmriSkedarit(String emriSkedarit) { this.emriSkedarit = emriSkedarit; }

    public String getFormatiSkedarit() { return formatiSkedarit; }
    public void setFormatiSkedarit(String formatiSkedarit) { this.formatiSkedarit = formatiSkedarit; }

    public Integer getMadhesiaKb() { return madhesiaKb; }
    public void setMadhesiaKb(Integer madhesiaKb) { this.madhesiaKb = madhesiaKb; }

    public LocalDate getDataNgarkimit() { return dataNgarkimit; }
    public void setDataNgarkimit(LocalDate dataNgarkimit) { this.dataNgarkimit = dataNgarkimit; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public Double getPerputhshmeriaMePozicionin() { return perputhshmeriaMePozicionin; }
    public void setPerputhshmeriaMePozicionin(Double perputhshmeriaMePozicionin) { this.perputhshmeriaMePozicionin = perputhshmeriaMePozicionin; }
}
