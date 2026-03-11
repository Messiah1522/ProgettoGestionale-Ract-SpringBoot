package com.progetto.gestionale.util;
import com.progetto.gestionale.entity.Progetto;
import com.progetto.gestionale.entity.Reparto;
import com.progetto.gestionale.entity.Dipendente;
import com.progetto.gestionale.dto.ProgettoDTO;
import com.progetto.gestionale.dto.DipendenteDTO;
import com.progetto.gestionale.dto.RepartoDTO;

import java.util.List;
import java.util.stream.Collectors;

/*
    Classe di utilit che si occupa di convertire
    Entity <-> DTO
    Non contiene Logica e non accede al DB
 */
public class MapperUtils {
    // Costruttore privato per impedire l'istanziazione
    private MapperUtils(){}

    /*
        Convento un'entità Reparto in RepartoDTO.
        Serve quando si restituiscono dati al client
     */
    public static RepartoDTO toRepartoDTO(Reparto reparto){
        if(reparto == null){
            return null;
        }
        RepartoDTO dto = new RepartoDTO();
        //Copio i campi semplici
        dto.setId(reparto.getId());
        dto.setNome(reparto.getNome());
        dto.setDescrizione(reparto.getDescrizione());

        return dto;
    }

    /*
        Converto un DTO in un'entità Reparto.
        Serve quando riceviamo dati dal client
     */
    public static Reparto toRepartoEntity(RepartoDTO dto){
        if(dto == null){
            return null;
        }
        Reparto reparto = new Reparto();

        reparto.setNome(dto.getNome());
        reparto.setDescrizione(dto.getDescrizione());

        return reparto;
    }

    //Dipendente. Non si espone l'intero reparto ma solo il suo id
    public static DipendenteDTO toDipendenteDTO(Dipendente dipendente){
        if(dipendente == null){
            return null;
        }

        DipendenteDTO dto = new DipendenteDTO();

        dto.setId(dipendente.getId());
        dto.setNome(dipendente.getNome());
        dto.setCognome(dipendente.getCognome());
        dto.setEmail(dipendente.getEmail());
        dto.setStipendio(dipendente.getStipendio());
        dto.setDataAssunzione(dipendente.getDataAssunzione());

        //se il dipendente ha un reparto passiamo solo l'id
        if(dipendente.getReparto() != null){
            dto.setRepartoId(dipendente.getReparto().getId());
        }
        return dto;
    }

    public static Dipendente toDipendenteEntity(DipendenteDTO dto){
        if(dto == null){
            return null;
        }
        Dipendente dipendente = new Dipendente();

        dipendente.setNome(dto.getNome());
        dipendente.setCognome(dto.getCognome());
        dipendente.setEmail(dto.getEmail());
        dipendente.setStipendio(dto.getStipendio());
        dipendente.setDataAssunzione(dto.getDataAssunzione());

        return dipendente;
    }

    //converto Prodeggo in ProgettoDTO
    public static ProgettoDTO toProgettoDTO(Progetto progetto){
        if(progetto == null){
            return null;
        }
        ProgettoDTO dto = new ProgettoDTO();

        dto.setId(progetto.getId());
        dto.setNome(progetto.getNome());
        dto.setBudget(progetto.getBudget());
        dto.setDataInizio(progetto.getDataInizio());
        dto.setDataFine(progetto.getDataFine());

        /*
        se il progetto ha un reparto associato esponiamo
        solo l'id
         */
        if(progetto.getReparto() != null){
            dto.setRepartoId(progetto.getReparto().getId());
        }
        /*
        Se il progetto ha dipendenti associati, trasformo la
        lista di dipendente in lista di id
         */
        if(progetto.getDipendenti() != null){
            List<Long> dipendentiId = progetto.getDipendenti()
                    .stream()
                    .map(Dipendente::getId)
                    .collect(Collectors.toList());
            dto.setDipendentiId(dipendentiId);


        }
        return dto;
    }

    /*
    converto un ProgettoDTO in entià Progetto.
    qui non si setta id, reparto, dipendenti. Le operazioni
    vanno farre nel service perché richiedono accesso al DB
     */
    public static Progetto toProgettoEntity(ProgettoDTO dto){
        if(dto == null){
            return null;
        }

        Progetto progetto = new Progetto();

        progetto.setNome(dto.getNome());
        progetto.setBudget(dto.getBudget());
        progetto.setDataInizio(dto.getDataInizio());
        progetto.setDataFine(dto.getDataFine());

        return progetto;
    }
}
