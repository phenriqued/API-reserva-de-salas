package com.github.phenriqued.api_reserva_salas.DTOs.Autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @Email(message = "Por favor, forneça um endereço de e-mail válido")
        String email,
        @NotBlank(message = "Por favor, forneça uma senha válida")
        String password) {
}
