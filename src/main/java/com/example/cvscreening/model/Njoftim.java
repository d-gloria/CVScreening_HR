package com.example.cvscreening.model;

import java.time.LocalDate;

public class Njoftim {
    private Integer idNjoftim;
    private Integer idPerdorues;
    private String menyraDergimit;
    private String permbajtja;
    private LocalDate dataDergimit;
    private String statusiLeximit;

    public Integer getIdNjoftim() { return idNjoftim; }
    public void setIdNjoftim(Integer idNjoftim) { this.idNjoftim = idNjoftim; }

    public Integer getIdPerdorues() { return idPerdorues; }
    public void setIdPerdorues(Integer idPerdorues) { this.idPerdorues = idPerdorues; }

    public String getMenyraDergimit() { return menyraDergimit; }
    public void setMenyraDergimit(String menyraDergimit) { this.menyraDergimit = menyraDergimit; }

    public String getPermbajtja() { return permbajtja; }
    public void setPermbajtja(String permbajtja) { this.permbajtja = permbajtja; }

    public LocalDate getDataDergimit() { return dataDergimit; }
    public void setDataDergimit(LocalDate dataDergimit) { this.dataDergimit = dataDergimit; }

    public String getStatusiLeximit() { return statusiLeximit; }
    public void setStatusiLeximit(String statusiLeximit) { this.statusiLeximit = statusiLeximit; }
}
