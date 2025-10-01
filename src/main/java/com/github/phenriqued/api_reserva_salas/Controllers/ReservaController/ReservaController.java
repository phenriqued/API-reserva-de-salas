package com.github.phenriqued.api_reserva_salas.Controllers.ReservaController;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.CriarDadosReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.DadoSalaReservaId;
import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.DadoUsuarioReservaId;
import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.DadosReserva;
import com.github.phenriqued.api_reserva_salas.Services.ReservaService.ReservaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reserva")

@AllArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<DadosReserva> criarReserva(@RequestBody @Valid CriarDadosReserva criarDadosReserva, UriComponentsBuilder builder){
        var reservaCriada = reservaService.criarReserva(criarDadosReserva);
        URI uri = builder.path("/reserva/"+reservaCriada.codigoReserva()).buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(reservaCriada);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<DadosReserva> listarReservaPorId(@PathVariable(value = "id")  Long id){
        return ResponseEntity.ok(reservaService.listarPorId(id));
    }
    @GetMapping("/listar/sala")
    public ResponseEntity<List<DadosReserva>> listarTodasReservaPorSala(@RequestBody DadoSalaReservaId salaId){
        return ResponseEntity.ok(reservaService.listarTodasReservaPorSala(salaId));
    }
    @GetMapping("/listar/usuario")
    public ResponseEntity<List<DadosReserva>> listarTodasReservaPorUsuario(@RequestBody DadoUsuarioReservaId usuarioId){
        return ResponseEntity.ok(reservaService.listarTodasReservaPorUsuario(usuarioId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizarReserva(@PathVariable(value = "id") Long reservaId, @RequestBody DadosAtualizarReserva dadosAtualizarReserva){
        reservaService.atualizarReserva(reservaId, dadosAtualizarReserva);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable(value = "id") Long id){
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

}
