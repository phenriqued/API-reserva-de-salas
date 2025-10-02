package com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO;

import java.time.LocalDateTime;

public record DadosAtualizarReserva(
        Long usuarioId,
        Long salaId,
        LocalDateTime inicioReserva,
        LocalDateTime fimReserva,
        String statusReserva
){
}
