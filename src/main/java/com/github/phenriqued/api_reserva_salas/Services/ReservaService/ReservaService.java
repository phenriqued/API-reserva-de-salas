package com.github.phenriqued.api_reserva_salas.Services.ReservaService;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.*;
import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.StatusReserva;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.ReservaRepository.ReservaRepository;
import com.github.phenriqued.api_reserva_salas.Services.SalaService.SalaService;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

@AllArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final SalaService salaService;

    @Transactional(rollbackFor = Exception.class)
    public DadosReserva criarReserva(JwtAuthenticationToken token, CriarDadosReserva dadosReserva) {
        var usuario = findUsuarioByEmail(token.getName());
        var sala = salaService.findById(dadosReserva.salaId());
        validarPeriodoReserva(sala, dadosReserva.inicioReserva(), dadosReserva.fimReserva());
        var reserva = reservaRepository.save(new Reserva(usuario, sala, dadosReserva));
        return new DadosReserva(reserva);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelarReserva(Long id, JwtAuthenticationToken token) {
        var reserva = findById(id);
        validarProprietarioReserva(reserva, token);
        if(reserva.getStatusReserva() != StatusReserva.ATIVA)
            throw new BusinessRuleException("Não é possível cancelar uma reserva que não esteja ativa");

        reserva.setStatusReserva(StatusReserva.CANCELADA);
        reservaRepository.flush();
    }

    @Transactional(rollbackFor = Exception.class)
    public void reativarReserva(Long id, JwtAuthenticationToken token) {
        var reserva = findById(id);
        validarProprietarioReserva(reserva, token);
        if(reserva.getStatusReserva() != StatusReserva.CANCELADA){
            throw new BusinessRuleException("Não é possível ativar uma reservar que foi concluida ou está ativa");
        }
        validarPeriodoReserva(reserva.getSala(), reserva.getInicioReserva(), reserva.getFimReserva());
        reserva.setStatusReserva(StatusReserva.ATIVA);
        reservaRepository.flush();
    }

    @Transactional(readOnly = true)
    public DadosReserva listarPorId(Long id) {
        var reserva = findById(id);
        return new DadosReserva(reserva);
    }
    @Transactional(readOnly = true)
    public List<DadosReserva> listarTodasReservaPorSala(DadoSalaReservaId salaId, Pageable pageable) {
        return reservaRepository.findAllBySalaIdAndStatusReserva(salaId.salaId(), StatusReserva.ATIVA, pageable).stream().map(DadosReserva::new).toList();
    }
    @Transactional(readOnly = true)
    public List<DadosReserva> listarTodasReservaPorUsuario(JwtAuthenticationToken token, Pageable pageable) {
        var usuarioId = findUsuarioByEmail(token.getName()).getId();
        return reservaRepository.findAllByUsuarioIdAndStatusReserva(usuarioId, StatusReserva.ATIVA, pageable).stream().map(DadosReserva::new).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void atualizarReserva(Long reservaId, DadosAtualizarReserva dadosAtualizarReserva, JwtAuthenticationToken token){
        var reserva = findById(reservaId);
        validarProprietarioReserva(reserva, token);
        Sala sala = dadosAtualizarReserva.salaId() != null
                ? salaService.findById(dadosAtualizarReserva.salaId())
                : reserva.getSala();

        Usuario usuario = dadosAtualizarReserva.usuarioId() != null
                ? usuarioService.findById(dadosAtualizarReserva.usuarioId())
                : null;

        if(dadosAtualizarReserva.salaId() != null || dadosAtualizarReserva.inicioReserva() != null || dadosAtualizarReserva.fimReserva() != null){
            validarPeriodoAtualizacao(dadosAtualizarReserva, sala, reserva);
        }
        reserva.atualizarReserva(sala, usuario, dadosAtualizarReserva);
        reservaRepository.save(reserva);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReserva(Long id, JwtAuthenticationToken token) {
        var reserva = findById(id);
        validarProprietarioReserva(reserva, token);
        reservaRepository.deleteById(id);
    }

    private void validarPeriodoAtualizacao(DadosAtualizarReserva dados, Sala sala, Reserva reserva){
        if (dados.salaId() != null && (dados.inicioReserva() == null || dados.fimReserva() == null)) {
            throw new BusinessRuleException("Tanto data de inicio quanto data do fim da reserva devem ser diferente de null");
        }
        LocalDateTime inicio = Optional.ofNullable(dados.inicioReserva()).orElse(reserva.getInicioReserva());
        LocalDateTime fim = Optional.ofNullable(dados.fimReserva()).orElse(reserva.getFimReserva());

        validarPeriodoReserva(sala, inicio, fim);
    }
    private void validarPeriodoReserva(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null)
            throw new BusinessRuleException("Datas de início e fim são obrigatórias.");

        if(reservaRepository.existsReservaConflitante(sala.getId(), inicio, fim)) {
            throw new BusinessRuleException("Já existe uma reserva entre o periodo marcado!");
        }
    }
    private void validarProprietarioReserva(Reserva reserva,JwtAuthenticationToken token){
        var usuario = findUsuarioByEmail(token.getName());
        if (!Objects.equals(reserva.getUsuario().getId(), usuario.getId()))
            throw new BusinessRuleException("Não é possível cancelar uma reserva que não pertence a "+usuario.getNome());
    }
    private Reserva findById(Long id){
        return reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));
    }
    private Usuario findUsuarioByEmail(String email){
        return usuarioService.findByEmail(email);
    }
}
