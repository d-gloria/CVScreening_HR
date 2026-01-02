package com.example.cvscreening.model;

import java.time.LocalDate;

public class Perdorues {
    private Integer idPerdorues;
    private String emri;
    private String mbiemri;
    private String adresaEmail;
    private String fjalekalimi;
    private String roli;
    private LocalDate dataRegjistrimit;
    private String gjendja;

    public Integer getIdPerdorues() { return idPerdorues; }
    public void setIdPerdorues(Integer idPerdorues) { this.idPerdorues = idPerdorues; }

    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }

    public String getMbiemri() { return mbiemri; }
    public void setMbiemri(String mbiemri) { this.mbiemri = mbiemri; }

    public String getAdresaEmail() { return adresaEmail; }
    public void setAdresaEmail(String adresaEmail) { this.adresaEmail = adresaEmail; }

    public String getFjalekalimi() { return fjalekalimi; }
    public void setFjalekalimi(String fjalekalimi) { this.fjalekalimi = fjalekalimi; }

    public String getRoli() { return roli; }
    public void setRoli(String roli) { this.roli = roli; }

    public LocalDate getDataRegjistrimit() { return dataRegjistrimit; }
    public void setDataRegjistrimit(LocalDate dataRegjistrimit) { this.dataRegjistrimit = dataRegjistrimit; }

    public String getGjendja() { return gjendja; }
    public void setGjendja(String gjendja) { this.gjendja = gjendja; }
}
