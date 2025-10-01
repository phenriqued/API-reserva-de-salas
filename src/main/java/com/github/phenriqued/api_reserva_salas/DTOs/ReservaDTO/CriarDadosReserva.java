package com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarDadosReserva(
        @NotNull
        Long usuarioId,
        @NotNull
        Long salaId,
        @NotNull
        LocalDateTime inicioReserva,
        @NotNull
        LocalDateTime fimReserva) {
}
