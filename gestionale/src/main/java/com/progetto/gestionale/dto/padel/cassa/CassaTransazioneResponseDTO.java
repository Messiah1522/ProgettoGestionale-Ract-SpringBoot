package com.progetto.gestionale.dto.padel.cassa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CassaTransazioneResponseDTO {

    private Long id;
    private BigDecimal totale;
    private LocalDateTime dataTransazione;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public LocalDateTime getDataTransazione() {
        return dataTransazione;
    }

    public void setDataTransazione(LocalDateTime dataTransazione) {
        this.dataTransazione = dataTransazione;
    }
}
