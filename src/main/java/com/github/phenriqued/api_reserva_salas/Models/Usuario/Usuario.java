package com.github.phenriqued.api_reserva_salas.Models.Usuario;

import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @Column(nullable = false)
    private String nome;

    // email do usuário
    @Setter
    @Column(unique = true, nullable = false)
    private String email;

    // senha do usuário
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> listReservas;

}
