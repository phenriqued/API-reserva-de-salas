package com.github.phenriqued.api_reserva_salas.Controllers.UsuarioController;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.AtualizarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.DadosUsuario;
import com.github.phenriqued.api_reserva_salas.Services.UsuarioService.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("usuario")

@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<DadosUsuario> criarUsuario(@RequestBody @Valid CriarDadosUsuario criarDadosUsuario, UriComponentsBuilder uriComponentsBuilder){
        var usuarioCriado = usuarioService.criarUsuario(criarDadosUsuario);
        URI uri = uriComponentsBuilder.path("/usuario/"+usuarioCriado.getId()).buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(new DadosUsuario(usuarioCriado.getNome(), usuarioCriado.getEmail()));
    }

    @GetMapping
    public ResponseEntity<List<DadosUsuario>> buscarTodosUsuarios(){
        return ResponseEntity.ok(usuarioService.buscarTodosUsuarios());
    }

    @GetMapping("{id}")
    public ResponseEntity<DadosUsuario> pegarUsuarioPeloID(@PathVariable("id") Long id){
        return ResponseEntity.ok(usuarioService.pegarUsuarioPeloId(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> atualizarUsuario(@PathVariable("id") Long id, @RequestBody @Valid AtualizarDadosUsuario atualizarDadosUsuario){
        usuarioService.atualizarUsuario(id, atualizarDadosUsuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DadosUsuario> deletarUsuarioPeloID(@PathVariable("id") Long id){
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }


}
