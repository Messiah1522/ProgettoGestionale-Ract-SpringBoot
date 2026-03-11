package com.progetto.gestionale.dto;

import java.time.LocalDate;
import java.util.List;

public class ProgettoDTO {

    private Long id;

    private String nome;

    private double budget;

    private LocalDate dataInizio;

    private LocalDate dataFine;

    private Long repartoId;

    private List<Long> dipendentiId;

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

    public Long getRepartoId() {
        return repartoId;
    }

    public List<Long> getDipendentiId() {
        return dipendentiId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setRepartoId(Long repartoId) {
        this.repartoId = repartoId;
    }

    public void setDipendentiId(List<Long> dipendentiId) {
        this.dipendentiId = dipendentiId;
    }
}
