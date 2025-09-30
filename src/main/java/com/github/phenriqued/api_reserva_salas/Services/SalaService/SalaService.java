package com.github.phenriqued.api_reserva_salas.Services.SalaService;

import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.AtualizarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.DadosSala;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Repositories.SalaRepository.SalaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service

@AllArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;

    @Transactional
    public Sala criarSala(@Valid CriarDadosSala dadosSala) {
        Sala novaSala = new Sala(dadosSala);
        return salaRepository.save(novaSala);
    }

    public List<DadosSala> listarTodasSalas() {
        return
            salaRepository.findAll().stream().map(DadosSala::new).toList();
    }
    public DadosSala listarSalaPorId(Long id) {
        return salaRepository.findById(id).map(DadosSala::new).orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
    }

    public void atualizarDadosSala(Long id, AtualizarDadosSala dadosSala){
        if(Objects.isNull(dadosSala)){
            return;
        }
        Sala atualizarSala = salaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
        atualizarSala.atualizarSala(dadosSala);
        salaRepository.save(atualizarSala);
    }

    public void deletarSala(Long id) {
        Sala deletarSala = salaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
        salaRepository.deleteById(deletarSala.getId());
    }
}
