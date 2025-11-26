package com.github.phenriqued.api_reserva_salas.Services.ReservaService;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.CriarDadosReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.StatusReserva;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.ReservaRepository.ReservaRepository;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarEAtivarReservaTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioService usuarioService;
    @InjectMocks
    private ReservaService reservaService;

    private Usuario usuarioTest = new Usuario("Teste", "teste@email.com", "123456");
    private Sala salaTest = new Sala(new CriarDadosSala("Sala", 10, "Rua Teste", "Sala de teste"));

    void setarId(){
        ReflectionTestUtils.setField(usuarioTest, "id", 1L);
        ReflectionTestUtils.setField(salaTest, "id", 1L);
    }

    @Test
    @DisplayName("Deveria cancelar uma reserva quando a mesma existe e está ativa")
    void cancelarReserva() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(null, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);
        ReflectionTestUtils.setField(reserva, "id", 1L);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        reservaService.cancelarReserva(1L, token);

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals(StatusReserva.CANCELADA, reserva.getStatusReserva());
    }
    @Test
    @DisplayName("não deveria cancelar uma reserva quando a mesma existe, mas já está concluida")
    void naoDeveriaCancelarReservaQuandoEstaConcluida() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);
        ReflectionTestUtils.setField(reserva, "id", 1L);
        reserva.setStatusReserva(StatusReserva.CONCLUIDA);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Exception exception = assertThrows(BusinessRuleException.class, () -> reservaService.cancelarReserva(1L, token));

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals("Não é possível cancelar uma reserva que não esteja ativa", exception.getMessage());
        assertEquals(StatusReserva.CONCLUIDA, reserva.getStatusReserva());
    }
    @Test
    @DisplayName("não deveria cancelar uma reserva quando a mesma existe, mas já está cancelada")
    void naoDeveriaCancelarReservaQuandoEstaCancelada() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);
        ReflectionTestUtils.setField(reserva, "id", 1L);
        reserva.setStatusReserva(StatusReserva.CANCELADA);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Exception exception = assertThrows(BusinessRuleException.class, () -> reservaService.cancelarReserva(1L, token));

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals("Não é possível cancelar uma reserva que não esteja ativa", exception.getMessage());
        assertEquals(StatusReserva.CANCELADA, reserva.getStatusReserva());
    }

    @Test
    @DisplayName("Deveria reativar uma reserva quando a mesma existe e está cancelada e o periodo marcado ainda está disponível")
    void reativarReserva() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);
        ReflectionTestUtils.setField(reserva, "id", 1L);
        reserva.setStatusReserva(StatusReserva.CANCELADA);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.existsReservaConflitante(1L, dadosReserva.inicioReserva(), dadosReserva.fimReserva())).thenReturn(false);

        reservaService.reativarReserva(1L, token);

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals(StatusReserva.ATIVA, reserva.getStatusReserva());
    }
    @Test
    @DisplayName("não deveria reativar uma reserva quando a mesma existe, mas já está ativa ou concluida")
    void naoDeveriaReativarReservaQuandoStatusReservaAtivaOuConcluida() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);
        ReflectionTestUtils.setField(reserva, "id", 1L);
        reserva.setStatusReserva(StatusReserva.CONCLUIDA);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Exception exception = assertThrows(BusinessRuleException.class, () -> reservaService.reativarReserva(1L, token));

        verify(reservaRepository, times(1)).findById(1L);
        assertEquals("Não é possível ativar uma reservar que foi concluida ou está ativa", exception.getMessage());
        assertEquals(StatusReserva.CONCLUIDA, reserva.getStatusReserva());
    }


}