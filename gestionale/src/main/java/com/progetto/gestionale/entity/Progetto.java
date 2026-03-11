package com.progetto.gestionale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "progetti")
public class Progetto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Positive
    private double budget;

    private LocalDate dataInizio;

    private LocalDate dataFine;

    @ManyToOne
    /*
        molti progetti possono appartenere a un reparto
     */
    @JoinColumn(name = "reparto_id")
    private Reparto reparto;

    @ManyToMany
    /*
        relazioni molti a molti tra progetti e dipendenti
        hibernate creerà una tabella di join automaticamente
     */
    @JoinTable(
            name = "progetti_dipendenti",
            joinColumns = @JoinColumn(name = "progetto_id"),
            inverseJoinColumns = @JoinColumn(name = "dipendente_id")
    )
    /*
        la tabella ponte conterrà progetto_id e dipendente_id
     */
    private List<Dipendente> dipendenti;

    private LocalDateTime dataCreazione;
    private LocalDateTime dataUltimoAggiornamento;

    @PrePersist
    public void onCreate(){
        dataCreazione = LocalDateTime.now();
        dataUltimoAggiornamento = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate(){
        dataUltimoAggiornamento = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getBudget() {
        return budget;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public List<Dipendente> getDipendenti() {
        return dipendenti;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public LocalDateTime getDataUltimoAggiornamento() {
        return dataUltimoAggiornamento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    public void setDipendenti(List<Dipendente> dipendenti) {
        this.dipendenti = dipendenti;
    }
}
