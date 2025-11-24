package com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO;

import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRegistradoDTO(
        @NotNull
        Long id,
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String token) {
    public UsuarioRegistradoDTO(Usuario usuario, String token){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), token);
    }

}
