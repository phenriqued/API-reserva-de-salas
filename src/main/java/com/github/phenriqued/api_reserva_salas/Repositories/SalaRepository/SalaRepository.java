package com.github.phenriqued.api_reserva_salas.Repositories.SalaRepository;

import com.github.phenriqued.api_reserva_salas.Models.Sala.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}
