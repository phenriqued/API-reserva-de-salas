package com.github.phenriqued.api_reserva_salas.Services.UsuarioService;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.UsuarioRegistradoDTO;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.UsuarioRepository.UsuarioRepository;
import com.github.phenriqued.api_reserva_salas.Services.Authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationService authenticationService;

    public RegistrarUsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder, AuthenticationService authenticationService) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
        this.authenticationService = authenticationService;
    }

    @Transactional(rollbackFor = Exception.class)
    public UsuarioRegistradoDTO criarUsuario(@Valid CriarDadosUsuario dadosUsuario) {
        String passwordEncoder = encoder.encode(dadosUsuario.password());
        Usuario novoUsuario = new Usuario(dadosUsuario.name(), dadosUsuario.email(), passwordEncoder);
        usuarioRepository.save(novoUsuario);
        var authentication = usuarioAutenticado(novoUsuario);
        String token = authenticationService.authenticate(authentication);
        return new UsuarioRegistradoDTO(novoUsuario, token);
    }

    private Authentication usuarioAutenticado(Usuario usuario){
        var authentication = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


}
