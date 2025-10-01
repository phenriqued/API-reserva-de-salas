package com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO;

import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.DadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.DadosUsuario;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.StatusReserva;

import java.time.LocalDateTime;

public record DadosReserva(
        Long codigoReserva,
        DadosUsuario usuario,
        DadosSala sala,
        LocalDateTime dataInicioReserva,
        LocalDateTime dataFimReserva,
        StatusReserva statusReserva) {

    public DadosReserva(Reserva entity){
        this(entity.getId(), new DadosUsuario(entity.getUsuario()), new DadosSala(entity.getSala()),
                entity.getInicioReserva(), entity.getFimReserva(), entity.getStatusReserva());
    }

}
