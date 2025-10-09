package com.github.phenriqued.api_reserva_salas.Services.ReservaService;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.CriarDadosReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.ReservaRepository.ReservaRepository;
import com.github.phenriqued.api_reserva_salas.Services.SalaService.SalaService;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeletarReservaTest {

    @Mock
    private ReservaRepository reservaRepository;
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
    @DisplayName("Deveria deletar uma reserva quando a mesma existe")
    void deletarReserva() {
        setarId();

        reservaService.deleteReserva(1L);

        verify(reservaRepository, times(1)).deleteById(1L);
    }


}