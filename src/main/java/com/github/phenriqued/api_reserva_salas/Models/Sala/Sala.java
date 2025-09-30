package com.github.phenriqued.api_reserva_salas.Models.Sala;

import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tb_salas")

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Sala {

    // o banco de dados gera um valor incremental automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nome da sala
    @Column(nullable = false, unique = true)
    private String name;

    // capacidade total da sala
    @Column(nullable = false)
    private Integer capacidadeTotal;

    //localização da sala
    @Column(nullable = false)
    private String localizacao;

    // uma descrição opcional da sala
    private String descricao;

    // disponivel para efetuar a reserva ou não
    @Column(nullable = false)
    private Boolean disponivelParaReserva;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> listSalaReservada;

}
