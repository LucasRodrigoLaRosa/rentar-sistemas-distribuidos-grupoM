package com.example.backend.repository;

import com.example.backend.model.Reserva;
import com.example.backend.model.enums.estadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.vehiculo.id = :vehiculoId
          AND r.estado = com.example.backend.model.enums.EstadoReserva.CONFIRMADA
          AND r.fechaHoraInicio < :fin
          AND r.fechaHoraFin > :inicio
    """)
    boolean existeSolapamiento(
            @Param("vehiculoId") Long vehiculoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByClienteIdAndEstadoIn(Long clienteId, List<estadoReserva> estados);
}
