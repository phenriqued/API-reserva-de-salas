package com.github.phenriqued.api_reserva_salas.Models.Sala;

import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.AtualizarDadosSala;
import com.github.phenriqued.api_reserva_salas.DTOs.SalaDTO.CriarDadosSala;
import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

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
    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    // capacidade total da sala
    @Setter
    @Column(nullable = false)
    private Integer capacidadeTotal;

    //localização da sala
    @Setter
    @Column(nullable = false)
    private String localizacao;

    // uma descrição opcional da sala
    @Setter
    private String descricao;

    // disponivel para efetuar a reserva ou não
    @Setter
    @Column(nullable = false)
    private Boolean disponivelParaReserva;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> listSalaReservada;

    public Sala(CriarDadosSala dadosSala){
        this.name = dadosSala.nome();
        this.capacidadeTotal = dadosSala.capacidadeTotal();
        this.localizacao = dadosSala.localizacao();
        this.descricao = dadosSala.descricao();
        this.disponivelParaReserva = true;
    }

    public void atualizarSala(AtualizarDadosSala dadosSala) {
        setIfNotEmpty(dadosSala.nome(), this::setName);
        setIfNotEmpty(dadosSala.localizacao(), this::setLocalizacao);
        setIfNotEmpty(dadosSala.descricao(), this::setDescricao);
        if (dadosSala.capacidadeTotal() != null && dadosSala.capacidadeTotal() > 0){
            setCapacidadeTotal(dadosSala.capacidadeTotal());
        }
    }
    private void setIfNotEmpty(String value, Consumer<String> setter){
        if (value != null && !value.isEmpty())
            setter.accept(value);
    }
}
