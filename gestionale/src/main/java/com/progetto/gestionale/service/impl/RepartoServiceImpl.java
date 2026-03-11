package com.progetto.gestionale.service.impl;

import com.progetto.gestionale.entity.Reparto;
import com.progetto.gestionale.service.RepartoService;
import com.progetto.gestionale.repository.RepartoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RepartoServiceImpl implements RepartoService {
        private final RepartoRepository repartoRepository;

    public RepartoServiceImpl(RepartoRepository repartoRepository) {
        this.repartoRepository = repartoRepository;
    }
    @Transactional
    @Override
    public Reparto creaReparto(Reparto reparto) {
        return repartoRepository.save(reparto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Reparto> trovaTutti() {
        return repartoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Reparto> trovaPerId(Long id) {
        return repartoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Reparto> trovaPerNome(String nome) {
        return repartoRepository.findByNome(nome);
    }

    @Transactional
    @Override
    public Reparto aggiornaReparto(Long id, Reparto repartoAggiornato) {
        Reparto reparto = repartoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reparto non trovato"));
            reparto.setNome(repartoAggiornato.getNome());
            reparto.setDescrizione(repartoAggiornato.getDescrizione());
            return repartoRepository.save(reparto);
    }

    @Transactional
    @Override
    public void elimina(Long id) {
        repartoRepository.deleteById(id);
    }
}
