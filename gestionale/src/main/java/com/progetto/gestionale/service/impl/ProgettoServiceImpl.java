package com.progetto.gestionale.service.impl;
import com.progetto.gestionale.dto.ProgettoDTO;
import com.progetto.gestionale.entity.Progetto;
import com.progetto.gestionale.entity.Reparto;
import com.progetto.gestionale.entity.Dipendente;
import com.progetto.gestionale.repository.DipendenteRepository;
import com.progetto.gestionale.repository.ProgettoRepository;
import com.progetto.gestionale.repository.RepartoRepository;
import com.progetto.gestionale.service.ProgettoService;
import com.progetto.gestionale.util.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProgettoServiceImpl implements ProgettoService {

    private final ProgettoRepository progettoRepository;
    private final RepartoRepository repartoRepository;
    private final DipendenteRepository dipendenteRepository;

    public ProgettoServiceImpl(ProgettoRepository progettoRepository,
                               RepartoRepository repartoRepository,
                               DipendenteRepository dipendenteRepository) {
        this.progettoRepository = progettoRepository;
        this.repartoRepository = repartoRepository;
        this.dipendenteRepository = dipendenteRepository;
    }

    @Transactional
    @Override
    public Progetto creaProgetto(ProgettoDTO dto) {
        if (progettoRepository.findByNome(dto.getNome()).isPresent()){
            throw new IllegalArgumentException("progetto gia esistente");
        }

        //converto il dto in entità base senza relazioni
        Progetto progetto = MapperUtils.toProgettoEntity(dto);

        // recupero il reparto da DB usando l'id del dto
        Reparto reparto = repartoRepository.findById(dto.getRepartoId())
                .orElseThrow(() -> new IllegalArgumentException("reparto non trovato"));

        //recupero tutti i dipendenti usando la lista di id del dto
        List<Dipendente> dipendenti = dipendenteRepository.findAllById(dto.getDipendentiId());

        //Associo il reparto al progetto
        progetto.setReparto(reparto);

        //assoccio i dipendenti al progetto (ManyToMany)
        progetto.setDipendenti(dipendenti);

        return progettoRepository.save(progetto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Progetto> trovaTutti() {
        return progettoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Progetto> trovaPerId(long id) {
        return progettoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Progetto> trovaPerNome(String nome) {
        return progettoRepository.findByNome(nome);
    }

    @Transactional
    @Override
    public Progetto aggiornaProgetto(Long id, Progetto progettoAggiornato) {

        Progetto progetto = progettoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("progetto non trovato"));
        progetto.setNome(progettoAggiornato.getNome());
        progetto.setBudget(progettoAggiornato.getBudget());
        progetto.setDataFine(progettoAggiornato.getDataFine());

        return progettoRepository.save(progetto);
    }

    @Transactional
    @Override
    public void elimina(Long id) {
        if(!progettoRepository.existsById(id)){
            throw new IllegalArgumentException("Progetto non trovato");
        }
        progettoRepository.deleteById(id);
    }
}
