package com.example.cvscreening.model;

import java.time.LocalDate;

public class RegjisterAuditi {
    private Integer idVeprimi;
    private Integer idPerdorues;
    private String objekti;
    private String veprimi;
    private LocalDate dataVeprimit;
    private String oraVeprimit;
    private String pershkrimiDetajuar;

    public Integer getIdVeprimi() { return idVeprimi; }
    public void setIdVeprimi(Integer idVeprimi) { this.idVeprimi = idVeprimi; }

    public Integer getIdPerdorues() { return idPerdorues; }
    public void setIdPerdorues(Integer idPerdorues) { this.idPerdorues = idPerdorues; }

    public String getObjekti() { return objekti; }
    public void setObjekti(String objekti) { this.objekti = objekti; }

    public String getVeprimi() { return veprimi; }
    public void setVeprimi(String veprimi) { this.veprimi = veprimi; }

    public LocalDate getDataVeprimit() { return dataVeprimit; }
    public void setDataVeprimit(LocalDate dataVeprimit) { this.dataVeprimit = dataVeprimit; }

    public String getOraVeprimit() { return oraVeprimit; }
    public void setOraVeprimit(String oraVeprimit) { this.oraVeprimit = oraVeprimit; }

    public String getPershkrimiDetajuar() { return pershkrimiDetajuar; }
    public void setPershkrimiDetajuar(String pershkrimiDetajuar) { this.pershkrimiDetajuar = pershkrimiDetajuar; }
}
