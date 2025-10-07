package com.github.phenriqued.api_reserva_salas.Controllers.SalaController;

import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.AtualizarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.DadosSala;
import com.github.phenriqued.api_reserva_salas.Services.SalaService.SalaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sala")

@AllArgsConstructor
public class SalaController {

    private final SalaService salaService;

    @PostMapping
    public ResponseEntity<DadosSala> criarSala(@RequestBody @Valid CriarDadosSala criarDadosSala, UriComponentsBuilder builder){
        var salaCriada = salaService.criarSala(criarDadosSala);
        URI uri = builder.path("/sala/"+salaCriada.getId()).buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(new DadosSala(salaCriada));
    }

    @GetMapping("listar")
    public ResponseEntity<List<DadosSala>> listarTodasSalas(@PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(salaService.listarTodasSalas(pageable));
    }
    @GetMapping("listar/{id}")
    public ResponseEntity<DadosSala> listarSalaPorId(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(salaService.listarSalaPorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizarSala(@PathVariable(value = "id") Long id, @RequestBody @Valid AtualizarDadosSala atualizarDadosSala){
        salaService.atualizarDadosSala(id, atualizarDadosSala);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSala(@PathVariable(value = "id") Long id){
        salaService.deletarSala(id);
        return ResponseEntity.noContent().build();
    }

}
