package com.github.phenriqued.api_reserva_salas.Services.ReservaService;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.CriarDadosReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.DadosAtualizarReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.ReservaRepository.ReservaRepository;
import com.github.phenriqued.api_reserva_salas.Services.SalaService.SalaService;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarReservaTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private SalaService salaService;
    @InjectMocks
    private ReservaService reservaService;

    private Usuario usuarioTest = new Usuario(new CriarDadosUsuario("Teste", "teste@email.com", "123456"));
    private Sala salaTest = new Sala(new CriarDadosSala("Sala", 10, "Rua Teste", "Sala de teste"));
    CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
    Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);

    void setarId(){
        ReflectionTestUtils.setField(usuarioTest, "id", 1L);
        ReflectionTestUtils.setField(salaTest, "id", 1L);
        ReflectionTestUtils.setField(reserva, "id", 1L);
    }

    @Test
    @DisplayName("Deveria atualizar o Periodo de uma reserva quando a mesma existe e dados de atualização estão corretos e não a reserva conflitante com periodo")
    void atualizarPeriodoReserva() {
        setarId();

        LocalDateTime novoPeriodoInicio = LocalDateTime.now().plusHours(1);
        LocalDateTime novoPeriodoFim = LocalDateTime.now().plusHours(2);

        DadosAtualizarReserva dadosAtualizar = new DadosAtualizarReserva(null, 1L,
                novoPeriodoInicio, novoPeriodoFim, null);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(salaService.findById(1L)).thenReturn(salaTest);
        when(reservaRepository.existsReservaConflitante(1L, dadosAtualizar.inicioReserva(), dadosAtualizar.fimReserva())).thenReturn(false);

        reservaService.atualizarReserva(1L, dadosAtualizar);

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals(novoPeriodoInicio, reserva.getInicioReserva());
        assertEquals(novoPeriodoFim, reserva.getFimReserva());
    }
    @Test
    @DisplayName("Deveria atualizar a Sala de uma reserva quando a mesma existe e sala não estivar ocupada")
    void atualizarSalaReserva() {
        setarId();

        Sala novaSala = new Sala(new CriarDadosSala("Nova Sala", 50, "Rua Teste", "Nova Sala de teste"));
        ReflectionTestUtils.setField(novaSala, "id", 2L);

        DadosAtualizarReserva dadosAtualizar = new DadosAtualizarReserva(null, 2L, null, null, null);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(salaService.findById(2L)).thenReturn(novaSala);
        when(reservaRepository.existsReservaConflitante(2L, reserva.getInicioReserva(), reserva.getFimReserva())).thenReturn(false);

        reservaService.atualizarReserva(1L, dadosAtualizar);

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals(novaSala.getName(), reserva.getSala().getName());
        assertEquals(novaSala.getCapacidadeTotal(), reserva.getSala().getCapacidadeTotal());
    }
    @Test
    @DisplayName("Deveria atualizar o usuário de uma reserva quando a mesma existe")
    void atualizarUsuarioReserva() {
        setarId();

        Usuario novoUsuario = new Usuario(new CriarDadosUsuario("New User Test", "test@email.com", "123456"));
        ReflectionTestUtils.setField(novoUsuario, "id", 2L);

        DadosAtualizarReserva dadosAtualizar = new DadosAtualizarReserva(2L, null, null, null, null);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(usuarioService.findById(2L)).thenReturn(novoUsuario);

        reservaService.atualizarReserva(1L, dadosAtualizar);

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals(novoUsuario.getNome(), reserva.getUsuario().getNome());
        assertEquals(novoUsuario.getEmail(), reserva.getUsuario().getEmail());
    }
    @Test
    @DisplayName("Nao deveria atualizar a reserva quando a mesma não existe")
    void naoDeveriaAtualizarReservaQuandoNaoExiste() {
        setarId();
        DadosAtualizarReserva dadosAtualizar = new DadosAtualizarReserva(null, null,
                LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(10), null);

        when(reservaRepository.findById(1L)).thenThrow(new EntityNotFoundException("Reserva não encontrada"));
        Exception exception = assertThrows(EntityNotFoundException.class, () -> reservaService.atualizarReserva(1L, dadosAtualizar));
        assertEquals("Reserva não encontrada", exception.getMessage());
    }
    @Test
    @DisplayName("Não deveria atualizar a Sala de uma reserva quando a mesma existe e sala já ocupada")
    void napDeveriaAtualizarSalaReservaQuandoSalaEstaOcupada() {
        setarId();
        Sala novaSala = new Sala(new CriarDadosSala("Nova Sala", 50, "Rua Teste", "Nova Sala de teste"));
        ReflectionTestUtils.setField(novaSala, "id", 2L);

        DadosAtualizarReserva dadosAtualizar = new DadosAtualizarReserva(null, 2L, null, null, null);

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(salaService.findById(2L)).thenReturn(novaSala);
        when(reservaRepository.existsReservaConflitante(2L, reserva.getInicioReserva(), reserva.getFimReserva())).thenReturn(true);

        Exception exception = assertThrows(BusinessRuleException.class, () -> reservaService.atualizarReserva(1L, dadosAtualizar));
        assertEquals("Já existe uma reserva entre o periodo marcado!", exception.getMessage());
    }



}