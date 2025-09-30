package com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO;

import jakarta.validation.constraints.Min;

public record AtualizarDadosSala(
        String nome,
        @Min(value = 1)
        Integer capacidadeTotal,
        String localizacao,
        String descricao){
}
