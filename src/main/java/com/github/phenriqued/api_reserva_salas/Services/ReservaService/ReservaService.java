package com.github.phenriqued.api_reserva_salas.Services.ReservaService;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.*;
import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.ReservaRepository.ReservaRepository;
import com.github.phenriqued.api_reserva_salas.Services.SalaService.SalaService;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service

@AllArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final SalaService salaService;

    @Transactional(rollbackFor = Exception.class)
    public DadosReserva criarReserva(CriarDadosReserva dadosReserva) {
        var usuario = usuarioService.findById(dadosReserva.usuarioId());
        var sala = salaService.findById(dadosReserva.salaId());
        validarPeriodoReserva(sala, dadosReserva.inicioReserva(), dadosReserva.fimReserva());
        var reserva = reservaRepository.save(new Reserva(usuario, sala, dadosReserva));
        return new DadosReserva(reserva);
    }
    @Transactional(readOnly = true)
    public DadosReserva listarPorId(Long id) {
        var reserva = reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));
        return new DadosReserva(reserva);
    }
    @Transactional(readOnly = true)
    public List<DadosReserva> listarTodasReservaPorSala(DadoSalaReservaId salaId, Pageable pageable) {
        return reservaRepository.findAllBySalaId(salaId.salaId(), pageable).stream().map(DadosReserva::new).toList();
    }
    @Transactional(readOnly = true)
    public List<DadosReserva> listarTodasReservaPorUsuario(DadoUsuarioReservaId usuarioId, Pageable pageable) {
        return reservaRepository.findAllByUsuarioId(usuarioId.usuarioId(), pageable).stream().map(DadosReserva::new).toList();
    }
    @Transactional(rollbackFor = Exception.class)
    public void atualizarReserva(Long reservaId, DadosAtualizarReserva dadosAtualizarReserva){
        var reserva = reservaRepository.findById(reservaId).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));

        Sala sala = dadosAtualizarReserva.salaId() != null
                ? salaService.findById(dadosAtualizarReserva.salaId())
                : null;

        Usuario usuario = dadosAtualizarReserva.usuarioId() != null
                ? usuarioService.findById(dadosAtualizarReserva.usuarioId())
                : null;

        if(dadosAtualizarReserva.inicioReserva() != null || dadosAtualizarReserva.fimReserva() != null){
            validarPeriodoReserva(sala, dadosAtualizarReserva.inicioReserva(), dadosAtualizarReserva.fimReserva());
        }
        reserva.atualizarReserva(sala, usuario, dadosAtualizarReserva);
        reservaRepository.save(reserva);
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    private void validarPeriodoReserva(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new BusinessRuleException("Tanto data de inicio quanto data do fim da reserva devem ser diferente de null");
        }else if(sala == null){
            throw new BusinessRuleException("Sala não pode ser null");
        }else if(reservaRepository.existsReservaConflitante(sala.getId(), inicio, fim)){
            throw new BusinessRuleException("Já existe uma reserva entre o periodo marcado!");
        }else if(fim.isBefore(LocalDateTime.now()) || inicio.isBefore(LocalDateTime.now())){
            throw new BusinessRuleException("O periodo não pode anteceder a data atual");
        }else if(fim.isBefore(inicio)){
            throw new BusinessRuleException("O periodo inicial deve anteceder o fim");
        }
    }

}
