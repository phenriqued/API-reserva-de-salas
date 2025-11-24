package com.github.phenriqued.api_reserva_salas.Controllers.AutenticacaoController;

import com.github.phenriqued.api_reserva_salas.DTOs.Autenticacao.LoginDTO;
import com.github.phenriqued.api_reserva_salas.DTOs.Autenticacao.TokenDTO;
import com.github.phenriqued.api_reserva_salas.Services.Authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("entrar")
public class LoginController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager manager;

    public LoginController(AuthenticationService authenticationService, AuthenticationManager manager) {
        this.authenticationService = authenticationService;
        this.manager = manager;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> autenticarUsuario(@RequestBody @Valid LoginDTO loginDTO){
        var userAuthentication = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        var authentication = manager.authenticate(userAuthentication);
        var token = authenticationService.authenticate(authentication);
        return ResponseEntity.ok(new TokenDTO(token));
    }


}
