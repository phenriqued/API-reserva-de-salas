package com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO;

import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;

public record DadosUsuario(
        String nome,
        String email) {
    public DadosUsuario(Usuario entity){
        this(entity.getNome(), entity.getEmail());
    }
}
