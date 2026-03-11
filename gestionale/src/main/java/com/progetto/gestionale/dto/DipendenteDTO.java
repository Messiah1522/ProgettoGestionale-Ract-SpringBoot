package com.progetto.gestionale.dto;

import java.time.LocalDate;


public class DipendenteDTO {

    private Long id;

    private String nome;

    private String cognome;

    private String email;

    private String ruolo;

    private double stipendio;

    private LocalDate dataAssunzione;

    /* id del reparto di appartenenza, non l'entity reparto
    per evitare coupling. Il coupling è il grado di appartenenza
    tra due componenti software. Meglio un basso coupling

    */
    private Long repartoId;


    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getRuolo() {
        return ruolo;
    }

    public double getStipendio() {
        return stipendio;
    }

    public LocalDate getDataAssunzione() {
        return dataAssunzione;
    }

    public Long getRepartoId() {
        return repartoId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public void setStipendio(double stipendio) {
        this.stipendio = stipendio;
    }

    public void setDataAssunzione(LocalDate dataAssunzione) {
        this.dataAssunzione = dataAssunzione;
    }

    public void setRepartoId(Long repartoId) {
        this.repartoId = repartoId;
    }
}
