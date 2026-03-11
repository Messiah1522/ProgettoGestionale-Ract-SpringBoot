package com.progetto.gestionale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dipendenti")
public class Dipendente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @Email
    @Column(unique = true)
    private String email;

    private String ruolo;

    @Positive
    private double stipendio;

    private LocalDate dataAssunzione;

    @ManyToOne
    /*
        relazione molti a uno. Cioè molti dipendenti
        possono appartenere a un reparto. Crea una foeign key
     */
    @JoinColumn(name = "reparto_id")
    /*
        joincolumn indica nome della colonna nel database
        e qui verà creata la foreign key
     */
    private Reparto reparto;

    private LocalDateTime dataCreazione;

    private LocalDateTime dataUltumoAggiornamento;

    @PrePersist
    public void onCreate(){
        dataCreazione = LocalDateTime.now();
        dataUltumoAggiornamento = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        dataUltumoAggiornamento = LocalDateTime.now();
    }

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

    public Reparto getReparto() {
        return reparto;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltumoAggiornamento() {
        return dataUltumoAggiornamento;
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

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }
}
