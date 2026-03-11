package com.progetto.gestionale.dto.padel;

public class PartecipanteCalendarioDTO {

    private Long utenteId;
    private String nome;
    private String cognome;
    private Boolean confermato;

    public Long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Long utenteId) {
        this.utenteId = utenteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Boolean getConfermato() {
        return confermato;
    }

    public void setConfermato(Boolean confermato) {
        this.confermato = confermato;
    }
}
