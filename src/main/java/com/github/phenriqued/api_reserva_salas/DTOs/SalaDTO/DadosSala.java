package com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO;

import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;

public record DadosSala(
        String nome,
        Integer capacidadeTotal,
        String localizacao,
        String descricao) {
    public DadosSala(Sala entity){
        this(entity.getName(), entity.getCapacidadeTotal(), entity.getLocalizacao(), entity.getDescricao());
    }
}
