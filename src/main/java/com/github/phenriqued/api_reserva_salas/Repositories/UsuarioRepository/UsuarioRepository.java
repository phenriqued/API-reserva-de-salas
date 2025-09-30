package com.github.phenriqued.api_reserva_salas.Repositories.UsuarioRepository;

import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
