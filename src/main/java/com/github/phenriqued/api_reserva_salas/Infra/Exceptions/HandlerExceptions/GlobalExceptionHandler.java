package com.github.phenriqued.api_reserva_salas.Infra.Exceptions.HandlerExceptions;

import com.github.phenriqued.api_reserva_salas.DTOs.ExceptionsDTO.DataErrorValidationDTO;
import com.github.phenriqued.api_reserva_salas.DTOs.ExceptionsDTO.ErrorDTO;
import com.github.phenriqued.api_reserva_salas.Infra.Exceptions.BusinessRuleException.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DataErrorValidationDTO>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        var errors = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(DataErrorValidationDTO::new).toList());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorDTO> handlerBusinessRuleException(BusinessRuleException exception){
        return ResponseEntity.badRequest().body(new ErrorDTO(exception.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handlerEntityNotFoundException(EntityNotFoundException exception){
        return ResponseEntity.status(404).body(new ErrorDTO(exception.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> tratarErroIntegridade(DataIntegrityViolationException exception){
        String mensagemErro = "Dados inválidos: O registro já existe ou viola uma restrição de integridade.";
        return ResponseEntity.badRequest().body(new ErrorDTO(mensagemErro));
    }


}
