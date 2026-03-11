package com.progetto.gestionale.service.padel.cassa.impl;

import com.progetto.gestionale.dto.padel.UtenteLiteDTO;
import com.progetto.gestionale.dto.padel.cassa.CassaRigaRequestDTO;
import com.progetto.gestionale.dto.padel.cassa.CassaTransazioneCreateRequestDTO;
import com.progetto.gestionale.dto.padel.cassa.CassaTransazioneResponseDTO;
import com.progetto.gestionale.entity.Prodotto;
import com.progetto.gestionale.entity.padel.PartecipazionePartita;
import com.progetto.gestionale.entity.padel.Partita;
import com.progetto.gestionale.entity.padel.RigaTransazioneCassa;
import com.progetto.gestionale.entity.padel.TransazioneCassa;
import com.progetto.gestionale.entity.padel.Utente;
import com.progetto.gestionale.entity.padel.enums.MetodoPagamento;
import com.progetto.gestionale.entity.padel.enums.TipoTransazioneCassa;
import com.progetto.gestionale.repository.ProdottoRepository;
import com.progetto.gestionale.repository.padel.PartitaPadelRepository;
import com.progetto.gestionale.repository.padel.TransazioneCassaRepository;
import com.progetto.gestionale.repository.padel.UtentePadelRepository;
import com.progetto.gestionale.service.padel.cassa.CassaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CassaServiceImpl implements CassaService {

    private final TransazioneCassaRepository transazioneCassaRepository;
    private final ProdottoRepository prodottoRepository;
    private final UtentePadelRepository utentePadelRepository;
    private final PartitaPadelRepository partitaPadelRepository;

    public CassaServiceImpl(
        TransazioneCassaRepository transazioneCassaRepository,
        ProdottoRepository prodottoRepository,
        UtentePadelRepository utentePadelRepository,
        PartitaPadelRepository partitaPadelRepository
    ) {
        this.transazioneCassaRepository = transazioneCassaRepository;
        this.prodottoRepository = prodottoRepository;
        this.utentePadelRepository = utentePadelRepository;
        this.partitaPadelRepository = partitaPadelRepository;
    }

    @Override
    @Transactional
    public CassaTransazioneResponseDTO registraTransazione(CassaTransazioneCreateRequestDTO request, String viewerRole) {
        requireAdminRole(viewerRole);

        if (request == null || request.getRighe() == null || request.getRighe().isEmpty()) {
            throw new IllegalArgumentException("Transazione non valida: inserire almeno una riga prodotto.");
        }

        TransazioneCassa transazione = new TransazioneCassa();
        transazione.setTipo(resolveTipo(request.getTipo()));
        transazione.setMetodoPagamento(resolveMetodo(request.getMetodoPagamento()));

        boolean clienteOccasionale = Boolean.TRUE.equals(request.getClienteOccasionale())
            || request.getClienteUtenteId() == null;
        transazione.setClienteOccasionale(clienteOccasionale);

        if (clienteOccasionale) {
            transazione.setNominativoClienteOccasionale(
                request.getNominativoClienteOccasionale() == null ? "Cliente Occasionale" : request.getNominativoClienteOccasionale()
            );
        } else {
            Utente cliente = utentePadelRepository.findById(request.getClienteUtenteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato."));
            transazione.setClientePagante(cliente);
        }

        List<RigaTransazioneCassa> righe = new ArrayList<>();
        for (CassaRigaRequestDTO rigaRequest : request.getRighe()) {
            if (rigaRequest.getProdottoId() == null || rigaRequest.getQuantita() == null || rigaRequest.getQuantita() <= 0) {
                throw new IllegalArgumentException("Riga prodotto non valida.");
            }

            Prodotto prodotto = prodottoRepository.findById(rigaRequest.getProdottoId())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato: " + rigaRequest.getProdottoId()));

            long disponibilita = prodotto.getQuantitaMagazzino() == null ? 0L : prodotto.getQuantitaMagazzino();
            if (disponibilita < rigaRequest.getQuantita()) {
                throw new IllegalArgumentException("Scorte insufficienti per prodotto: " + prodotto.getTitolo());
            }

            prodotto.setQuantitaMagazzino(disponibilita - rigaRequest.getQuantita());
            prodottoRepository.save(prodotto);

            RigaTransazioneCassa riga = new RigaTransazioneCassa();
            riga.setProdotto(prodotto);
            riga.setDescrizione(prodotto.getTitolo());
            riga.setQuantita(rigaRequest.getQuantita());
            double prezzo = prodotto.getPrezzo() == null ? 0.0 : prodotto.getPrezzo();
            riga.setPrezzoUnitario(BigDecimal.valueOf(prezzo));
            righe.add(riga);
        }

        for (RigaTransazioneCassa riga : righe) {
            transazione.addRiga(riga);
        }

        TransazioneCassa salvata = transazioneCassaRepository.save(transazione);

        CassaTransazioneResponseDTO response = new CassaTransazioneResponseDTO();
        response.setId(salvata.getId());
        response.setTotale(salvata.getTotale());
        response.setDataTransazione(salvata.getDataTransazione());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtenteLiteDTO> getGiocatoriPresenti(LocalDateTime referenceTime) {
        LocalDateTime now = referenceTime == null ? LocalDateTime.now() : referenceTime;
        LocalDateTime from = now.minusMinutes(30);
        LocalDateTime to = now.plusHours(2);

        List<Partita> partite = partitaPadelRepository.findByDataOraInizioBetweenOrderByDataOraInizioAsc(from, to);

        Map<Long, UtenteLiteDTO> result = new LinkedHashMap<>();
        for (Partita partita : partite) {
            if (partita.getPartecipanti() == null) {
                continue;
            }
            for (PartecipazionePartita partecipazione : partita.getPartecipanti()) {
                if (!Boolean.TRUE.equals(partecipazione.getConfermato()) || partecipazione.getUtente() == null) {
                    continue;
                }

                Long idUtente = partecipazione.getUtente().getId();
                if (result.containsKey(idUtente)) {
                    continue;
                }

                UtenteLiteDTO dto = new UtenteLiteDTO();
                dto.setId(idUtente);
                dto.setNome(partecipazione.getUtente().getNome());
                dto.setCognome(partecipazione.getUtente().getCognome());
                result.put(idUtente, dto);
            }
        }

        return new ArrayList<>(result.values());
    }

    private TipoTransazioneCassa resolveTipo(String raw) {
        if (raw == null || raw.isBlank()) {
            return TipoTransazioneCassa.VENDITA_MAGAZZINO;
        }

        try {
            return TipoTransazioneCassa.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
            return TipoTransazioneCassa.VENDITA_MAGAZZINO;
        }
    }

    private MetodoPagamento resolveMetodo(String raw) {
        if (raw == null || raw.isBlank()) {
            return MetodoPagamento.CONTANTI;
        }

        try {
            return MetodoPagamento.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
            return MetodoPagamento.CONTANTI;
        }
    }

    private void requireAdminRole(String viewerRole) {
        if (viewerRole == null) {
            throw new IllegalArgumentException("Operazione consentita solo agli admin.");
        }
        String role = viewerRole.trim().toUpperCase(Locale.ROOT);
        if (!role.equals("ROLE_ADMIN") && !role.equals("ROLE_SUPERADMIN")) {
            throw new IllegalArgumentException("Operazione consentita solo agli admin.");
        }
    }
}
