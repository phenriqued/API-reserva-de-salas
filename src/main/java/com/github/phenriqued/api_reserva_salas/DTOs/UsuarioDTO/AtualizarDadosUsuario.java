package com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO;

import jakarta.validation.constraints.Email;

public record AtualizarDadosUsuario(
        String nome,
        @Email
        String email,
        String password
) {
}
