package com.github.phenriqued.api_reserva_salas.Models.Reserva;

import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_salas")

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Reserva {

    // o banco de dados gera um valor incremental automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sala para que foi efetuada a reserva
    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;

    // o usuário que efetuou a reserva
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // data do inicio da reserva
    @Column(nullable = false)
    private LocalDateTime inicioReserva;

    // data do final da reserva
    @Column(nullable = false)
    private LocalDateTime fimReserva;

    // status da reserva
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva statusReserva;



}
