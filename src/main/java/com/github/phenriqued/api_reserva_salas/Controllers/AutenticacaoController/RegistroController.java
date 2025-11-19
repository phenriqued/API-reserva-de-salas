package com.github.phenriqued.api_reserva_salas.Controllers.AutenticacaoController;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.UsuarioRegistradoDTO;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.RegistrarUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("registrar")
public class RegistroController {


    private final RegistrarUsuarioService registrarService;

    public RegistroController(RegistrarUsuarioService registrarService) {
        this.registrarService = registrarService;
    }

    @PostMapping
    public ResponseEntity<UsuarioRegistradoDTO> registarUsuario(@RequestBody @Valid CriarDadosUsuario criarUsuarioDTO, UriComponentsBuilder builder){
        var usuario = registrarService.criarUsuario(criarUsuarioDTO);
        URI uri = builder.path("/profile/"+usuario.id()).buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

}
