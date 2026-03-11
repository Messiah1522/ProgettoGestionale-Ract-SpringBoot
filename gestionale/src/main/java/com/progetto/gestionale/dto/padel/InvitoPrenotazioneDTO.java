package com.progetto.gestionale.dto.padel;

import com.progetto.gestionale.entity.padel.enums.StatoInvitoPartita;

import java.time.LocalDateTime;

public class InvitoPrenotazioneDTO {

    private Long invitoId;
    private Long destinatarioId;
    private String destinatarioNome;
    private String destinatarioCognome;
    private StatoInvitoPartita stato;
    private LocalDateTime dataScadenza;

    public Long getInvitoId() {
        return invitoId;
    }

    public void setInvitoId(Long invitoId) {
        this.invitoId = invitoId;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getDestinatarioNome() {
        return destinatarioNome;
    }

    public void setDestinatarioNome(String destinatarioNome) {
        this.destinatarioNome = destinatarioNome;
    }

    public String getDestinatarioCognome() {
        return destinatarioCognome;
    }

    public void setDestinatarioCognome(String destinatarioCognome) {
        this.destinatarioCognome = destinatarioCognome;
    }

    public StatoInvitoPartita getStato() {
        return stato;
    }

    public void setStato(StatoInvitoPartita stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDateTime dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
}
