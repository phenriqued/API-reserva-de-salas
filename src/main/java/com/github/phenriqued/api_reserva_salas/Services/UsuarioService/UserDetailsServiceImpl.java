package com.github.phenriqued.api_reserva_salas.Services.UsuarioService;

import com.github.phenriqued.api_reserva_salas.Models.Usuario.UserDetailsImpl;
import com.github.phenriqued.api_reserva_salas.Repositories.UsuarioRepository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username).map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("Não foi possível encontrar o usuario"));
    }
}
