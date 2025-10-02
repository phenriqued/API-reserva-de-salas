package com.github.phenriqued.api_reserva_salas.Models.Reserva;

import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.DadosAtualizarReserva;
import com.github.phenriqued.api_reserva_salas.DTOs.ReservaDTO.CriarDadosReserva;
import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import com.github.phenriqued.api_reserva_salas.Models.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_reservas")

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

    public Reserva(Usuario usuario, Sala sala, CriarDadosReserva dadosReserva){
        this.usuario = usuario;
        this.sala = sala;
        this.inicioReserva = dadosReserva.inicioReserva();
        this.fimReserva = dadosReserva.fimReserva();
        this.statusReserva = StatusReserva.ATIVA;
    }

    public void atualizarReserva(Sala sala, Usuario usuario, DadosAtualizarReserva dadosReserva){
        setSala(sala);
        setUsuario(usuario);
        setInicioReserva(dadosReserva.inicioReserva());
        setFimReserva(dadosReserva.fimReserva());
        setStatusReserva(dadosReserva.statusReserva());
    }

    public void setSala(Sala sala){
        if(Objects.nonNull(sala))
            this.sala = sala;
    }
    public void setUsuario(Usuario usuario){
        if (Objects.nonNull(usuario))
            this.usuario = usuario;
    }
    public void setInicioReserva(LocalDateTime inicioReserva){
        if (Objects.nonNull(inicioReserva))
            this.inicioReserva = inicioReserva;
    }
    public void setFimReserva(LocalDateTime fimReserva){
        if (Objects.nonNull(fimReserva))
            this.fimReserva = fimReserva;
    }
    public void setStatusReserva(String status){
        if (Objects.nonNull(status) && !status.trim().isEmpty())
            this.statusReserva = StatusReserva.valueOf(status);
    }


}
