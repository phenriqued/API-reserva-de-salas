package com.github.phenriqued.api_reserva_salas.Models.Usuario;

import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.AtualizarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.DTOs.UsuarioDTO.CriarDadosUsuario;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

@Entity
@Table(name = "tb_usuarios")

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Usuario {

    // id sequencial para identificação do usuário, optei por utilizar ID de valor incremental pela simplicidade da aplicação.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nome do usuário
    @Setter
    @Column(nullable = false)
    private String nome;

    // email do usuário
    @Setter
    @Column(unique = true, nullable = false)
    private String email;

    // senha do usuário
    @Setter
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> listReservas;

    public Usuario(CriarDadosUsuario dadosUsuario){
        this.nome = dadosUsuario.name();
        this.email = dadosUsuario.email();
        this.password = dadosUsuario.password();
    }

    public void atualizarDados(AtualizarDadosUsuario atualizarDados) {
        setIfNotEmpty(atualizarDados.nome(), this::setNome);
        setIfNotEmpty(atualizarDados.email(), this::setEmail);
        setIfNotEmpty(atualizarDados.password(), this::setPassword);
    }
    private void setIfNotEmpty(String value, Consumer<String> setter){
        if (value != null && !value.isEmpty())
            setter.accept(value);
    }


}
