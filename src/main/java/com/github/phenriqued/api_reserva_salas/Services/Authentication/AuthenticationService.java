package com.github.phenriqued.api_reserva_salas.Services.Authentication;

import com.github.phenriqued.api_reserva_salas.Infra.Security.JWT.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    public String authenticate(Authentication authentication){
        return jwtService.generatedToken(authentication);
    }

}
