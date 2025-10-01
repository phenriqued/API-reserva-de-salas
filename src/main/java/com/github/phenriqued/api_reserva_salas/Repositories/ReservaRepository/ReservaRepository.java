package com.github.phenriqued.api_reserva_salas.Repositories.ReservaRepository;

import com.github.phenriqued.api_reserva_salas.Models.Reserva.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    //inicioExistente < fimNova AND fimExistente > inicioNova
    @Query("""
           SELECT CASE WHEN COUNT(reserva) > 0 THEN TRUE ELSE FALSE END 
           FROM Reserva reserva
           WHERE reserva.sala.id = :salaId
                AND reserva.statusReserva = 'ATIVA'
                AND reserva.inicioReserva < :fimReserva
                AND reserva.fimReserva > :inicioReserva     
    """)
    boolean existsReservaConflitante(@Param("salaId") Long salaId,
                                     @Param("inicioReserva") LocalDateTime inicioReserva,
                                     @Param("fimReserva") LocalDateTime fimReserva);

    List<Reserva> findAllBySalaId(Long salaId);

    List<Reserva> findAllByUsuarioId(Long usuarioId);
}
