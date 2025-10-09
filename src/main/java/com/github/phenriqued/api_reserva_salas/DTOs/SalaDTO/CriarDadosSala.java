package com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Valid
public record CriarDadosSala(
        @NotBlank
        String nome,
        @NotNull
        @Min(value = 1)
        Integer capacidadeTotal,
        @NotBlank
        String localizacao,
        String descricao) {
}
