package com.github.phenriqued.api_reserva_salas.Services.UsuarioService;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.AtualizarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.DadosUsuario;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.UsuarioRepository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<DadosUsuario> listarTodosUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable).stream().map(DadosUsuario::new).toList();
    }
    @Transactional(readOnly = true)
    public DadosUsuario listarUsuarioPeloId(Long id) {
        return usuarioRepository.findById(id).map(DadosUsuario::new).orElseThrow(EntityNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado, verifique o ID!"));
    }
    @Transactional(rollbackFor = Exception.class)
    public void atualizarUsuario(Long id, AtualizarDadosUsuario atualizarDados) {
        if (Objects.isNull(atualizarDados)){
            return;
        }
        var usuario = usuarioRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        usuario.atualizarDados(atualizarDados);
        usuarioRepository.save(usuario);
    }
    @Transactional(rollbackFor = Exception.class)
    public void deletarUsuario(Long id){
        usuarioRepository.deleteById(id);
    }

}
