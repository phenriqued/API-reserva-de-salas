package com.github.phenriqued.api_reserva_salas.Services.ReservaService;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.CriarDadosReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.StatusReserva;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarReservaTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private SalaService salaService;
    @InjectMocks
    private ReservaService reservaService;

    private Usuario usuarioTest = new Usuario("Teste", "teste@email.com", "123456");
    private Sala salaTest = new Sala(new CriarDadosSala("Sala", 10, "Rua Teste", "Sala de teste"));

    void setarId(){
        ReflectionTestUtils.setField(usuarioTest, "id", 1L);
        ReflectionTestUtils.setField(salaTest, "id", 1L);
    }

    @Test
    @DisplayName("Deveria criar uma reserva quando os dados estão corretos")
    void criarReserva() {
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(null, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(salaService.findById(1L)).thenReturn(salaTest);
        when(reservaRepository.existsReservaConflitante(salaTest.getId(), dadosReserva.inicioReserva(), dadosReserva.fimReserva())).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        reservaService.criarReserva(token, dadosReserva);

        verify(usuarioService, times(1)).findByEmail("teste@email.com");
        verify(salaService, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
        assertEquals(StatusReserva.ATIVA, reserva.getStatusReserva());
    }
    @Test
    @DisplayName("não deveria criar uma reserva quando usuario não existe")
    void naoDeveriaCriarReservaQuandoUsuarioNaoExiste() {
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenThrow(new EntityNotFoundException("Usuário não encontrado, verifique o ID!"));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> reservaService.criarReserva(token, dadosReserva));
        assertEquals("Usuário não encontrado, verifique o ID!", exception.getMessage());
        verify(reservaRepository,never()).save(any(Reserva.class));
    }
    @Test
    @DisplayName("não deveria criar uma reserva quando sala não existe")
    void naoDeveriaCriarReservaQuandoSalaNaoExiste() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(salaService.findById(1L)).thenThrow(new EntityNotFoundException("Sala não encontrada"));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> reservaService.criarReserva(token, dadosReserva));
        assertEquals("Sala não encontrada", exception.getMessage());
        verify(reservaRepository,never()).save(any(Reserva.class));
    }
    @Test
    @DisplayName("Não deveria criar uma reserva quando já existe uma reserva marcado no periodo solicitado")
    void naoDeveriaCriarReservaQuandoJaExisteUmReservaMarcadaNoPeriodo() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(null, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(salaService.findById(1L)).thenReturn(salaTest);
        when(reservaRepository.existsReservaConflitante(1L, dadosReserva.inicioReserva(), dadosReserva.fimReserva())).thenReturn(true);


        Exception exception = assertThrows(BusinessRuleException.class, () -> reservaService.criarReserva(token, dadosReserva));
        assertEquals("Já existe uma reserva entre o periodo marcado!", exception.getMessage());
        verify(usuarioService, times(1)).findByEmail("teste@email.com");
        verify(salaService, times(1)).findById(1L);
        verify(reservaRepository,never()).save(reserva);
    }
    @Test
    @DisplayName("Não deveria criar uma reserva quando o periodo antecede a data atual")
    void naoDeveriaCriarReservaQuandoPeriodoAntecedeDataAtual() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);
        LocalDateTime dataIncorreta =  LocalDateTime.now().minusHours(1);
        CriarDadosReserva dadosReserva = new CriarDadosReserva(null, 1L, dataIncorreta, LocalDateTime.now().plusHours(2));
        Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(salaService.findById(1L)).thenReturn(salaTest);

        Exception exception = assertThrows(BusinessRuleException.class, () -> reservaService.criarReserva(token, dadosReserva));
        assertEquals("O periodo não pode anteceder a data atual", exception.getMessage());
        verify(usuarioService, times(1)).findByEmail("teste@email.com");
        verify(salaService, times(1)).findById(1L);
        verify(reservaRepository,never()).save(reserva);
    }
    @Test
    @DisplayName("Não deveria permitir data final antes da inicial no DTO")
    void naoDeveriaCriarDTOQuandoFimAntecedeInicio() {

        LocalDateTime inicio = LocalDateTime.now().plusHours(1);
        LocalDateTime fim = LocalDateTime.now().plusMinutes(10);

        Exception exception = assertThrows(BusinessRuleException.class, () -> new CriarDadosReserva(1L, 1L, inicio, fim));
        assertEquals("O periodo inicial deve anteceder o fim", exception.getMessage());
    }

}