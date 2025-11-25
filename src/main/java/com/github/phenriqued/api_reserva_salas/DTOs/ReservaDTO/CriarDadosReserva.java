package com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO;

import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarDadosReserva(
        Long usuarioId,
        @NotNull
        Long salaId,
        @NotNull
        @Future(message = "A data/hora da reserva deve ser no futuro")
        LocalDateTime inicioReserva,
        @NotNull
        @Future(message = "A data/hora da reserva deve ser no futuro")
        LocalDateTime fimReserva) {
    public CriarDadosReserva{
        if(fimReserva.isBefore(inicioReserva)){
            throw new BusinessRuleException("O periodo inicial deve anteceder o fim");
        }
    }


}
