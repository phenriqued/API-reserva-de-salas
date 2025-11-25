package com.github.phenriqued.api_reserva_salas.Controllers.ReservaController;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.*;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.UserDetailsImpl;
import com.github.phenriqued.api_reserva_salas.Services.ReservaService.ReservaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public ResponseEntity<DadosReserva> criarReserva(@RequestBody @Valid CriarDadosReserva criarDadosReserva, UriComponentsBuilder builder,
                                                     JwtAuthenticationToken token){
        var reservaCriada = reservaService.criarReserva(token, criarDadosReserva);
        URI uri = builder.path("/reserva/"+reservaCriada.codigoReserva()).buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(reservaCriada);
    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable(value = "id")  Long id, JwtAuthenticationToken token){
        reservaService.cancelarReserva(id, token);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/reativar/{id}")
    public ResponseEntity<Void> ativarReserva(@PathVariable(value = "id")  Long id, JwtAuthenticationToken token){
        reservaService.reativarReserva(id, token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<DadosReserva> listarReservaPorId(@PathVariable(value = "id")  Long id, JwtAuthenticationToken token){
        return ResponseEntity.ok(reservaService.listarPorId(id));
    }
    @GetMapping("/listar/sala")
    public ResponseEntity<List<DadosReserva>> listarTodasReservaPorSala(@RequestBody DadoSalaReservaId salaId, @PageableDefault(size = 5) Pageable pageable){
        return ResponseEntity.ok(reservaService.listarTodasReservaPorSala(salaId, pageable));
    }
    @GetMapping("/listar/usuario")
    public ResponseEntity<List<DadosReserva>> listarTodasReservaPorUsuario(@PageableDefault(size = 5) Pageable pageable, JwtAuthenticationToken token){
        return ResponseEntity.ok(reservaService.listarTodasReservaPorUsuario(token, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizarReserva(@PathVariable(value = "id") Long reservaId, @RequestBody DadosAtualizarReserva dadosAtualizarReserva,
                                                 JwtAuthenticationToken token){
        reservaService.atualizarReserva(reservaId, dadosAtualizarReserva, token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable(value = "id") Long id, JwtAuthenticationToken token){
        reservaService.deleteReserva(id, token);
        return ResponseEntity.noContent().build();
    }

}
