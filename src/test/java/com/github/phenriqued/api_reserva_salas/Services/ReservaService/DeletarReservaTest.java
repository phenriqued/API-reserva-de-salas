package com.github.phenriqued.api_reserva_salas.Services.ReservaService;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.CriarDadosReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarReservaTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioService usuarioService;
    @InjectMocks
    private ReservaService reservaService;

    private Usuario usuarioTest = new Usuario("Teste", "teste@email.com", "123456");
    private Sala salaTest = new Sala(new CriarDadosSala("Sala", 10, "Rua Teste", "Sala de teste"));
    CriarDadosReserva dadosReserva = new CriarDadosReserva(1L, 1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2));
    Reserva reserva = new Reserva(usuarioTest, salaTest, dadosReserva);

    void setarId(){
        ReflectionTestUtils.setField(usuarioTest, "id", 1L);
        ReflectionTestUtils.setField(salaTest, "id", 1L);
        ReflectionTestUtils.setField(reserva, "id", 1L);
    }

    @Test
    @DisplayName("Deveria deletar uma reserva quando a mesma existe")
    void deletarReserva() {
        setarId();
        JwtAuthenticationToken token = Mockito.mock(JwtAuthenticationToken.class);

        when(token.getName()).thenReturn("teste@email.com");
        when(usuarioService.findByEmail("teste@email.com")).thenReturn(usuarioTest);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        reservaService.deleteReserva(1L, token);

        verify(reservaRepository, times(1)).deleteById(1L);
    }


}