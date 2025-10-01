package com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO;

import jakarta.validation.constraints.NotNull;

public record DadoUsuarioReservaId(
        @NotNull
        Long usuarioId
) {
}
