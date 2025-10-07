package com.github.phenriqued.api_reserva_salas.Controllers.ReservaController;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.*;
import com.github.phenriqued.api_reserva_salas.Services.ReservaService.ReservaService;
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

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable(value = "id")  Long id){
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Void> ativarReserva(@PathVariable(value = "id")  Long id){
        reservaService.reativarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<DadosReserva> listarReservaPorId(@PathVariable(value = "id")  Long id){
        return ResponseEntity.ok(reservaService.listarPorId(id));
    }
    @GetMapping("/listar/sala")
    public ResponseEntity<List<DadosReserva>> listarTodasReservaPorSala(@RequestBody DadoSalaReservaId salaId, @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(reservaService.listarTodasReservaPorSala(salaId, pageable));
    }
    @GetMapping("/listar/usuario")
    public ResponseEntity<List<DadosReserva>> listarTodasReservaPorUsuario(@RequestBody DadoUsuarioReservaId usuarioId, @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(reservaService.listarTodasReservaPorUsuario(usuarioId, pageable));
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
