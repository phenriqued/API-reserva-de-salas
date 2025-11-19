package com.github.phenriqued.api_reserva_salas.DTOs.Autenticacao;

import jakarta.validation.constraints.NotBlank;

public record TokenDTO(
        @NotBlank
        String token
) {
}
