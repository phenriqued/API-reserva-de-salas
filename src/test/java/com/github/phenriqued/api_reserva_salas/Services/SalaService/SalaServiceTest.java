package com.github.phenriqued.api_reserva_salas.Services.SalaService;

import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.AtualizarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Repositories.SalaRepository.SalaRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;
    @InjectMocks
    private SalaService salaService;
    private final Validator validator;

    SalaServiceTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deveria criar uma sala quando os dados estão corretos")
    void deveriaCriarSala() {
        CriarDadosSala salaDados = new CriarDadosSala("testeSala", 10, "Rua Teste", "Sala de teste");
        Sala sala = new Sala(salaDados);

        when(salaRepository.save(sala)).thenReturn(sala);

        salaService.criarSala(salaDados);
        verify(salaRepository, times(1)).save(sala);
    }
    @Test
    @DisplayName("Não Deveria criar sala quando detectar erro de validacao com dado incorreto")
    void deveDetectarErroDeValidacaoQuandoHouverDadoIncorreto() {
        CriarDadosSala salaInvalida = new CriarDadosSala("testeSala", 0, "Rua Teste", "Sala de teste");

        var violacoes = validator.validate(salaInvalida);

        assertFalse(violacoes.isEmpty());
    }

    @Test
    @DisplayName("Deveria atualizar os dados da sala quando os dados de atualização estão corretos")
    void atualizarDadosSala() {
        CriarDadosSala salaDados = new CriarDadosSala("testeSala", 10, "Rua Teste", "Sala de teste");
        Sala sala = new Sala(salaDados);
        ReflectionTestUtils.setField(sala, "id", 1L);

        AtualizarDadosSala atualizarDadosSala = new AtualizarDadosSala("teste", 20, null, null);
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(salaRepository.save(sala)).thenReturn(sala);

        salaService.atualizarDadosSala(1L, atualizarDadosSala);

        verify(salaRepository, times(1)).findById(1L);
        verify(salaRepository, times(1)).save(sala);
        assertEquals("teste", sala.getName());
        assertEquals(20, sala.getCapacidadeTotal());
    }
    @Test
    @DisplayName("Não Deveria atualizar sala quando detectar erro de validacao com dado incorreto")
    void naoDeveriaAtualizarDadosSalaQuandoHaDadosIncorretos() {
        AtualizarDadosSala atualizarDadosSalaInvalida = new AtualizarDadosSala(null, 0, null, null);

        var violacoes = validator.validate(atualizarDadosSalaInvalida);

        assertFalse(violacoes.isEmpty());
    }

    @Test
    @DisplayName("Deveria deletar uma sala quando a mesma existe e está correto")
    void deveriaDeletarSala() {
        CriarDadosSala salaDados = new CriarDadosSala("testeSala", 10, "Rua Teste", "Sala de teste");
        Sala sala = new Sala(salaDados);
        ReflectionTestUtils.setField(sala, "id", 1L);

        salaService.deletarSala(1L);

        verify(salaRepository, times(1)).deleteById(1L);
    }
}