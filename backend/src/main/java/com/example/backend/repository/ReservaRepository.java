package com.example.backend.repository;

import com.example.backend.model.Reserva;
import com.example.backend.model.enums.estadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Req. 4 (REST): Validación obligatoria de solapamiento de fechas
    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.vehiculo.idVehiculo = :vehiculoId
          AND r.estado = :estadoConfirmada
          AND (r.fechaInicio < :fin AND r.fechaFin > :inicio)
    """)
    boolean existeSolapamientoConEstado(
            @Param("vehiculoId") Long vehiculoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("estadoConfirmada") estadoReserva estadoConfirmada
    );

    // Método default con 3 parámetros para que compilen VehiculoService y ReservaService sin tocarlos
    default boolean existeSolapamiento(Long vehiculoId, LocalDateTime inicio, LocalDateTime fin) {
        return existeSolapamientoConEstado(vehiculoId, inicio, fin, estadoReserva.CONFIRMADA);
    }

    // Req. 5 (GraphQL): Consultas por cliente
    @Query("SELECT r FROM Reserva r WHERE r.cliente.idCliente = :clienteId")
    List<Reserva> findByClienteId(@Param("clienteId") Long clienteId);

    // Req. 7 (GraphQL): Historial del cliente (finalizadas y canceladas)
    @Query("SELECT r FROM Reserva r WHERE r.cliente.idCliente = :clienteId AND r.estado IN :estados")
    List<Reserva> findByClienteIdAndEstadoIn(
            @Param("clienteId") Long clienteId,
            @Param("estados") Collection<estadoReserva> estados
    );

    // Req. 5 (GraphQL): Búsqueda multicriterio con filtros opcionales
    @Query("""
        SELECT r FROM Reserva r
        WHERE (:clienteId IS NULL OR r.cliente.idCliente = :clienteId)
          AND (:vehiculoId IS NULL OR r.vehiculo.idVehiculo = :vehiculoId)
          AND (:estado IS NULL OR r.estado = :estado)
    """)
    List<Reserva> buscarReservas(
            @Param("clienteId") Long clienteId,
            @Param("vehiculoId") Long vehiculoId,
            @Param("estado") estadoReserva estado
    );
}