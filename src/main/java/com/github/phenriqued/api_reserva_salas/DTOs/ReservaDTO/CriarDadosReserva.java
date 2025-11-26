package com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO;

import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public record CriarDadosReserva(
        Long usuarioId,
        @NotNull
        Long salaId,
        @NotNull(message = "A data/hora não pode ser nula")
        @Future(message = "A data/hora da reserva deve ser no futuro")
        LocalDateTime inicioReserva,
        @NotNull(message = "A data/hora não pode ser nula")
        @Future(message = "A data/hora da reserva deve ser no futuro")
        LocalDateTime fimReserva) {
    public CriarDadosReserva{
        if(Objects.isNull(fimReserva) || Objects.isNull(inicioReserva))
            throw new BusinessRuleException("Ambas datas devem ser preenchidas");

        if(fimReserva.isBefore(inicioReserva))
            throw new BusinessRuleException("O periodo inicial deve anteceder o fim");
    }


}
