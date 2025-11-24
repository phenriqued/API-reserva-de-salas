package com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO;

import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record DadosAtualizarReserva(
        Long usuarioId,
        Long salaId,
        @Future(message = "A data/hora da reserva deve ser no futuro")
        LocalDateTime inicioReserva,
        @Future(message = "A data/hora da reserva deve ser no futuro")
        LocalDateTime fimReserva,
        String statusReserva){
    public DadosAtualizarReserva{
        if (inicioReserva != null && fimReserva != null){
            if(fimReserva.isBefore(inicioReserva)){
                throw new BusinessRuleException("O periodo inicial deve anteceder o fim");
            }
        }
    }
}
