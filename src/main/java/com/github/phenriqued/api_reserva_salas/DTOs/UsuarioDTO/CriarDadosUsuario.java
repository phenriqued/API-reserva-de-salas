package com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CriarDadosUsuario(
        @NotBlank(message = "Nome não pode ser em branco")
        String name,
        @Email(message = "Por favor, forneça um endereço de e-mail válido")
        String email,
        @NotBlank(message = "Por favor, forneça uma senha válida")
        String password) {
}
