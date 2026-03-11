package com.progetto.gestionale.dto.padel;

import com.progetto.gestionale.entity.padel.enums.StatoPartita;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartitaCalendarioDTO {

    private Long partitaId;
    private Long campoId;
    private String campoNome;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private StatoPartita stato;
    private List<PartecipanteCalendarioDTO> partecipanti = new ArrayList<>();

    public Long getPartitaId() {
        return partitaId;
    }

    public void setPartitaId(Long partitaId) {
        this.partitaId = partitaId;
    }

    public Long getCampoId() {
        return campoId;
    }

    public void setCampoId(Long campoId) {
        this.campoId = campoId;
    }

    public String getCampoNome() {
        return campoNome;
    }

    public void setCampoNome(String campoNome) {
        this.campoNome = campoNome;
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
    }

    public StatoPartita getStato() {
        return stato;
    }

    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }

    public List<PartecipanteCalendarioDTO> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(List<PartecipanteCalendarioDTO> partecipanti) {
        this.partecipanti = partecipanti;
    }
}
