package com.github.phenriqued.api_reserva_salas.Services.SalaService;

import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.AtualizarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.DadosSala;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Repositories.SalaRepository.SalaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service

@AllArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;

    @Transactional(rollbackFor = Exception.class)
    public Sala criarSala(@Valid CriarDadosSala dadosSala) {
        Sala novaSala = new Sala(dadosSala);
        return salaRepository.save(novaSala);
    }
    @Transactional(readOnly = true)
    public List<DadosSala> listarTodasSalas(Pageable pageable) {
        return
            salaRepository.findAll(pageable).stream().map(DadosSala::new).toList();
    }
    @Transactional(readOnly = true)
    public DadosSala listarSalaPorId(Long id) {
        return salaRepository.findById(id).map(DadosSala::new).orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
    }
    @Transactional(readOnly = true)
    public Sala findById(Long id) {
        return salaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
    }
    @Transactional(rollbackFor = Exception.class)
    public void atualizarDadosSala(Long id, AtualizarDadosSala dadosSala){
        if(Objects.isNull(dadosSala)){
            return;
        }
        Sala atualizarSala = salaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));
        atualizarSala.atualizarSala(dadosSala);
        salaRepository.save(atualizarSala);
    }
    @Transactional(rollbackFor = Exception.class)
    public void deletarSala(Long id) {
        salaRepository.deleteById(id);
    }
}
