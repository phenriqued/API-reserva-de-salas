package com.github.phenriqued.api_reserva_salas.Controllers.UsuarioController;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.AtualizarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.DadosUsuario;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuario")

@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<List<DadosUsuario>> listarTodosUsuarios(@PageableDefault(size = 5, sort = "nome")Pageable pageable){
        return ResponseEntity.ok(usuarioService.listarTodosUsuarios(pageable));
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<DadosUsuario> listarUsuarioPeloID(@PathVariable("id") Long id){
        return ResponseEntity.ok(usuarioService.listarUsuarioPeloId(id));
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<Void> atualizarUsuario(@RequestBody @Valid AtualizarDadosUsuario atualizarDadosUsuario,
                                                 JwtAuthenticationToken token){
        usuarioService.atualizarUsuario(atualizarDadosUsuario, token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<DadosUsuario> deletarUsuarioPeloID(JwtAuthenticationToken token){
        usuarioService.deletarUsuario(token);
        return ResponseEntity.noContent().build();
    }


}
