package com.progetto.gestionale.service.padel.impl;

import com.progetto.gestionale.dto.padel.PartecipantePagamentoDTO;
import com.progetto.gestionale.dto.padel.PartitaPagamentoDTO;
import com.progetto.gestionale.dto.padel.StatoPagamentoGenerale;
import com.progetto.gestionale.entity.padel.PagamentoPartita;
import com.progetto.gestionale.entity.padel.PartecipazionePartita;
import com.progetto.gestionale.entity.padel.Partita;
import com.progetto.gestionale.entity.padel.enums.StatoPagamento;
import com.progetto.gestionale.repository.padel.PartitaPadelRepository;
import com.progetto.gestionale.service.padel.PartitaPadelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class PartitaPadelServiceImpl implements PartitaPadelService {

    private final PartitaPadelRepository partitaPadelRepository;

    public PartitaPadelServiceImpl(PartitaPadelRepository partitaPadelRepository) {
        this.partitaPadelRepository = partitaPadelRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PartitaPagamentoDTO getPagamentiPartita(Long partitaId, Long viewerUserId, String viewerRole) {
        Partita partita = trovaPartita(partitaId);
        return toPartitaPagamentoDTO(partita, viewerUserId, isAdminRole(viewerRole));
    }

    @Override
    @Transactional
    public PartitaPagamentoDTO ricalcolaQuote(Long partitaId, Long viewerUserId, String viewerRole) {
        Partita partita = trovaPartita(partitaId);
        ricalcolaQuoteInterno(partita);
        Partita partitaSalvata = partitaPadelRepository.save(partita);
        return toPartitaPagamentoDTO(partitaSalvata, viewerUserId, isAdminRole(viewerRole));
    }

    private Partita trovaPartita(Long partitaId) {
        return partitaPadelRepository.findById(partitaId)
            .orElseThrow(() -> new IllegalArgumentException("Partita non trovata con id: " + partitaId));
    }

    private void ricalcolaQuoteInterno(Partita partita) {
        List<PartecipazionePartita> partecipanti = partita.getPartecipanti();
        if (partecipanti == null || partecipanti.isEmpty()) {
            throw new IllegalArgumentException("Impossibile ricalcolare: nessun partecipante registrato.");
        }

        long numeroConfermati = partecipanti.stream()
            .filter(p -> Boolean.TRUE.equals(p.getConfermato()))
            .count();

        List<PartecipazionePartita> partecipantiEffettivi = new ArrayList<>();
        if (numeroConfermati > 0) {
            for (PartecipazionePartita partecipazione : partecipanti) {
                if (Boolean.TRUE.equals(partecipazione.getConfermato())) {
                    partecipantiEffettivi.add(partecipazione);
                }
            }
        } else {
            partecipantiEffettivi.addAll(partecipanti);
        }

        if (partecipantiEffettivi.isEmpty()) {
            throw new IllegalArgumentException("Impossibile ricalcolare: nessun partecipante effettivo.");
        }

        if (partita.getCostoTotale() == null) {
            partita.setCostoTotale(BigDecimal.ZERO);
        }

        BigDecimal nuovaQuota = partita.getCostoTotale()
            .divide(BigDecimal.valueOf(partecipantiEffettivi.size()), 2, RoundingMode.HALF_UP);
        partita.setQuotaPerGiocatore(nuovaQuota);

        for (PartecipazionePartita partecipazione : partecipantiEffettivi) {
            if (!Boolean.TRUE.equals(partecipazione.getPagato())) {
                partecipazione.setQuotaDovuta(nuovaQuota);
                if (partecipazione.getQuotaPagata() == null) {
                    partecipazione.setQuotaPagata(BigDecimal.ZERO);
                }
                if (partecipazione.getTotalePagato() == null) {
                    partecipazione.setTotalePagato(partecipazione.getQuotaPagata());
                }
                partecipazione.aggiornaStatoPagamento();
            }
        }
    }

    private PartitaPagamentoDTO toPartitaPagamentoDTO(Partita partita, Long viewerUserId, boolean isAdmin) {
        List<PartecipazionePartita> partecipanti = partita.getPartecipanti() == null
            ? new ArrayList<>()
            : partita.getPartecipanti();

        if (!isAdmin && viewerUserId != null) {
            boolean viewerPartecipa = partecipanti.stream()
                .map(PartecipazionePartita::getUtente)
                .filter(Objects::nonNull)
                .anyMatch(utente -> viewerUserId.equals(utente.getId()));
            if (!viewerPartecipa) {
                throw new IllegalArgumentException("Utente non autorizzato a vedere i pagamenti di questa partita.");
            }
        }

        PartitaPagamentoDTO dto = new PartitaPagamentoDTO();
        dto.setPartitaId(partita.getId());
        dto.setDataOraInizio(partita.getDataOraInizio());
        dto.setDataOraFine(partita.getDataOraFine());
        dto.setCostoTotale(partita.getCostoTotale());
        dto.setQuotaPerGiocatore(partita.getQuotaPerGiocatore());

        List<PartecipantePagamentoDTO> partecipantiDTO = new ArrayList<>();
        int numeroPaganti = 0;

        for (PartecipazionePartita partecipazione : partecipanti) {
            PartecipantePagamentoDTO partecipanteDTO = new PartecipantePagamentoDTO();
            partecipanteDTO.setPartecipazioneId(partecipazione.getId());

            if (partecipazione.getUtente() != null) {
                Long utenteId = partecipazione.getUtente().getId();
                partecipanteDTO.setUtenteId(utenteId);
                boolean mostraDatiCompleti = isAdmin || (viewerUserId != null && viewerUserId.equals(utenteId));
                if (mostraDatiCompleti) {
                    partecipanteDTO.setNome(partecipazione.getUtente().getNome());
                    partecipanteDTO.setCognome(partecipazione.getUtente().getCognome());
                } else {
                    partecipanteDTO.setNome("Riservato");
                    partecipanteDTO.setCognome("");
                }
            } else {
                partecipanteDTO.setNome("Riservato");
                partecipanteDTO.setCognome("");
            }

            partecipanteDTO.setQuotaDaPagare(defaultBigDecimal(partecipazione.getQuotaDovuta()));
            partecipanteDTO.setQuotaPagata(defaultBigDecimal(partecipazione.getQuotaPagata()));
            partecipanteDTO.setMetodoPagamento(resolveMetodoPagamento(partecipazione));

            boolean hasPagato = Boolean.TRUE.equals(partecipazione.getPagato())
                || StatoPagamento.PAGATO.equals(partecipazione.getStatoPagamento());
            partecipanteDTO.setHasPagato(hasPagato);
            if (hasPagato) {
                numeroPaganti++;
            }

            partecipantiDTO.add(partecipanteDTO);
        }

        dto.setPartecipanti(partecipantiDTO);
        dto.setNumeroPartecipanti(partecipantiDTO.size());
        dto.setNumeroPaganti(numeroPaganti);
        dto.setStatoGenerale(calcolaStatoGenerale(numeroPaganti, partecipantiDTO.size()));
        return dto;
    }

    private String resolveMetodoPagamento(PartecipazionePartita partecipazione) {
        if (partecipazione.getMetodoPagamento() != null && !partecipazione.getMetodoPagamento().isBlank()) {
            return partecipazione.getMetodoPagamento();
        }
        if (partecipazione.getPagamenti() == null || partecipazione.getPagamenti().isEmpty()) {
            return null;
        }

        PagamentoPartita ultimoPagamento = partecipazione.getPagamenti()
            .stream()
            .max(Comparator.comparing(this::safeDataPagamento))
            .orElse(null);

        if (ultimoPagamento == null || ultimoPagamento.getMetodoPagamento() == null) {
            return null;
        }
        return ultimoPagamento.getMetodoPagamento().name();
    }

    private LocalDateTime safeDataPagamento(PagamentoPartita pagamentoPartita) {
        if (pagamentoPartita.getDataPagamento() == null) {
            return LocalDateTime.MIN;
        }
        return pagamentoPartita.getDataPagamento();
    }

    private StatoPagamentoGenerale calcolaStatoGenerale(int numeroPaganti, int numeroPartecipanti) {
        if (numeroPartecipanti <= 0) {
            return StatoPagamentoGenerale.ROSSO;
        }
        if (numeroPaganti == 0) {
            return StatoPagamentoGenerale.ROSSO;
        }
        if (numeroPaganti == numeroPartecipanti) {
            return StatoPagamentoGenerale.VERDE;
        }
        return StatoPagamentoGenerale.GIALLO;
    }

    private boolean isAdminRole(String viewerRole) {
        if (viewerRole == null) {
            return false;
        }
        String role = viewerRole.trim().toUpperCase(Locale.ROOT);
        return role.equals("ROLE_ADMIN") || role.equals("ROLE_SUPERADMIN");
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value;
    }
}
