package com.safo.safoapi.repository;

import com.safo.safoapi.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
           "WHERE a.quadra.idQuadra = :idQuadra " +
           "AND a.status != 'CANCELADO' " +
           "AND (:inicio < a.dataHoraFim AND :fim > a.dataHoraInicio)")
    boolean existeSobreposicao(@Param("idQuadra") Long idQuadra, 
                               @Param("inicio") LocalDateTime inicio, 
                               @Param("fim") LocalDateTime fim);
}