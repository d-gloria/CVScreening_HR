package com.example.cvscreening.model;

import java.time.LocalDate;

public class Raport {
    private Integer idRaport;
    private Integer idRekrutues;
    private LocalDate dataGjenerimit;
    private String llojiRaportit;
    private String pershkrimi;
    private String formatiRaportit;

    public Integer getIdRaport() { return idRaport; }
    public void setIdRaport(Integer idRaport) { this.idRaport = idRaport; }

    public Integer getIdRekrutues() { return idRekrutues; }
    public void setIdRekrutues(Integer idRekrutues) { this.idRekrutues = idRekrutues; }

    public LocalDate getDataGjenerimit() { return dataGjenerimit; }
    public void setDataGjenerimit(LocalDate dataGjenerimit) { this.dataGjenerimit = dataGjenerimit; }

    public String getLlojiRaportit() { return llojiRaportit; }
    public void setLlojiRaportit(String llojiRaportit) { this.llojiRaportit = llojiRaportit; }

    public String getPershkrimi() { return pershkrimi; }
    public void setPershkrimi(String pershkrimi) { this.pershkrimi = pershkrimi; }

    public String getFormatiRaportit() { return formatiRaportit; }
    public void setFormatiRaportit(String formatiRaportit) { this.formatiRaportit = formatiRaportit; }
}
