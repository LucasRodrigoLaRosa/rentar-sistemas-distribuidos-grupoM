package com.example.backend.repository;
import com.example.backend.model.Vehiculo;
import com.example.backend.model.enums.tipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo,Long>{
   
    
 //Estas conbinaciones estan en : 
 //Spring Data JPA Reference Documentation $\rightarrow$ "Defining Query Methods" $\rightarrow$ "Supported Query Keywords"
Optional<Vehiculo> findByIdAndActivoTrue(Long id);

    boolean existsByPatente(String patente);
    List<Vehiculo> findByActivoTrue();
    @Query("""
        SELECT v FROM Vehiculo v
        WHERE v.activo = true
          AND (:tipo IS NULL OR v.tipo = :tipo)
          AND (:marca IS NULL OR LOWER(v.marca) LIKE LOWER(CONCAT('%', :marca, '%')))
          AND (:modelo IS NULL OR LOWER(v.modelo) LIKE LOWER(CONCAT('%', :modelo, '%')))
          AND (:precioMin IS NULL OR v.precioDiario >= :precioMin)
          AND (:precioMax IS NULL OR v.precioDiario <= :precioMax)
          AND v.id NOT IN (
              SELECT r.vehiculo.id FROM Reserva r
              WHERE r.estado = com.example.backend.model.enums.EstadoReserva.CONFIRMADA
                AND r.fechaHoraInicio < :fin
                AND r.fechaHoraFin > :inicio
          )
    """)
    List<Vehiculo> buscarDisponibles(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("tipo") tipoVehiculo tipo,
            @Param("marca") String marca,
            @Param("modelo") String modelo,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
}
