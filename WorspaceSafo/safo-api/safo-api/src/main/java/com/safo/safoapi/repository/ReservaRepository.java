package com.safo.safoapi.repository;

import com.safo.safoapi.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.espaco.idEspaco = :espacoId " +
           "AND r.data = :data " +
           "AND r.status != 'CANCELADA' " +
           "AND (:inicio < r.horarioFim AND :fim > r.horarioInicio)")
    List<Reserva> buscarConflitos(@Param("espacoId") Long espacoId,
                                  @Param("data") LocalDate data,
                                  @Param("inicio") LocalTime inicio,
                                  @Param("fim") LocalTime fim);
}