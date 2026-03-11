package com.progetto.gestionale.service.padel.cassa;

import com.progetto.gestionale.dto.padel.UtenteLiteDTO;
import com.progetto.gestionale.dto.padel.cassa.CassaTransazioneCreateRequestDTO;
import com.progetto.gestionale.dto.padel.cassa.CassaTransazioneResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CassaService {

    CassaTransazioneResponseDTO registraTransazione(CassaTransazioneCreateRequestDTO request, String viewerRole);

    List<UtenteLiteDTO> getGiocatoriPresenti(LocalDateTime referenceTime);
}
