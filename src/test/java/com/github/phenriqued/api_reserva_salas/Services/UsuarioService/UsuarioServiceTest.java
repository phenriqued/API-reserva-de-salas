package com.github.phenriqued.api_reserva_salas.Services.UsuarioService;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.AtualizarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import com.github.phenriqued.api_reserva_salas.Repositories.UsuarioRepository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioService usuarioService;
    private final Validator validator;

    UsuarioServiceTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    @DisplayName("não deveria criar um usuário quando os dados estão incorretos")
    void naoDeveriaCriarUsuarioQuandoDadosEstaoIncorretos() {
        CriarDadosUsuario dadosUsuarioInvalido = new CriarDadosUsuario("UserTest", "testeEmailInvalido.com", "123456");

        var violacoes = validator.validate(dadosUsuarioInvalido);

        assertFalse(violacoes.isEmpty());
    }

    @Test
    @DisplayName("deveria atualizar um usuário quando os dados estão corretos")
    void atualizarUsuario() {
        CriarDadosUsuario dadosUsuario = new CriarDadosUsuario("UserTest", "teste@email.com", "123456");
        Usuario usuario = new Usuario("Teste", "Teste@email.com", "Teste123");
        ReflectionTestUtils.setField(usuario, "id", 1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        AtualizarDadosUsuario atualizarDados = new AtualizarDadosUsuario("new Name Test", null, null);

        usuarioService.atualizarUsuario(1L, atualizarDados);
        verify(usuarioRepository, times(1)).findById(1L);
        assertEquals("new Name Test", usuario.getNome());
    }
    @Test
    @DisplayName("não deveria atualizar um usuário quando os dados estão incorretos")
    void naoDeveriaAtualizarUsuarioQuandoDadosEstaoIncorretos() {
        AtualizarDadosUsuario dadosUsuario = new AtualizarDadosUsuario(null, "testeemailerrado.com", null);

        var violacoes = validator.validate(dadosUsuario);

        assertFalse(violacoes.isEmpty());
    }

    @Test
    @DisplayName("Deveria deletar uma usuario quando a mesmo existe e está correto")
    void deletarUsuario() {
        CriarDadosUsuario dadosUsuario = new CriarDadosUsuario("UserTest", "teste@email.com", "123456");
        Usuario usuario = new Usuario("Teste", "Teste@email.com", "Teste123");
        ReflectionTestUtils.setField(usuario, "id", 1L);

        usuarioService.deletarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}