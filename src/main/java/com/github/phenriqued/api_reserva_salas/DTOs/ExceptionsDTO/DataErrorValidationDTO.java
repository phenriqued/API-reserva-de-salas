package com.github.phenriqued.api_reserva_salas.DTOs.ExceptionsDTO;

import org.springframework.validation.FieldError;

public record DataErrorValidationDTO(
        String fieldError,
        String defaultMessage) {
    public DataErrorValidationDTO(FieldError fieldError){
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
