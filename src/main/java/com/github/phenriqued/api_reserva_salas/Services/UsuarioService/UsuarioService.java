package com.github.phenriqued.api_reserva_salas.Services.UsuarioService;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.AtualizarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.DadosUsuario;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.UsuarioRepository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service

@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario criarUsuario(@Valid CriarDadosUsuario criarDadosUsuario) {
        Usuario novoUsuario = new Usuario(criarDadosUsuario);
        return usuarioRepository.save(novoUsuario);
    }

    public DadosUsuario pegarUsuarioPeloId(Long id) {
        return usuarioRepository.findById(id).map(DadosUsuario::new).orElseThrow(EntityNotFoundException::new);
    }

    public List<DadosUsuario> buscarTodosUsuarios() {
        return usuarioRepository.findAll().stream().map(DadosUsuario::new).toList();
    }

    public void atualizarUsuario(Long id, AtualizarDadosUsuario atualizarDados) {
        if (Objects.isNull(atualizarDados)){
            return;
        }
        var usuario = usuarioRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        usuario.atualizarDados(atualizarDados);
        usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id){
        var usuario = usuarioRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        usuarioRepository.deleteById(usuario.getId());
    }

}
