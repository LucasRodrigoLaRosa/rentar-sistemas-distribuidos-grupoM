package com.example.backend.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.loader.internal.BaseNaturalIdLoadAccessImpl;
import com.example.backend.model.enums.estadoReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;



@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlquiler;

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaInicio; 
    
    @NotNull(message = "La fecha y hora de finalizacion es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    //LAZY para carga perezoza
    //Una entidad Reserva nunca puede existir sin un Cliente asociado.
    private Cliente cliente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVehiculo", nullable = false)
    private Vehiculo vehiculo;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioDiario;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importeTotal;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private estadoReserva estado;

public Reserva() {
}

public Reserva(LocalDateTime fechaInicio, LocalDateTime fechaFin, Cliente cliente, Vehiculo vehiculo,
        BigDecimal precioDiario, BigDecimal importeTotal) {
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.cliente = cliente;
    this.vehiculo = vehiculo;
    this.precioDiario = precioDiario;
    this.importeTotal = importeTotal;
    this.estado= estadoReserva.CONFIRMADA;
    

}

public Long getIdAlquiler() {
    return idAlquiler;
}

public void setIdAlquiler(Long idAlquiler) {
    this.idAlquiler = idAlquiler;
}

public LocalDateTime getFechaInicio() {
    return fechaInicio;
}

public LocalDateTime getFechaFin() {
    return fechaFin;
}

public Cliente getCliente() {
    return cliente;
}

public Vehiculo getVehiculo() {
    return vehiculo;
}

public BigDecimal getPrecioDiario() {
    return precioDiario;
}

public BigDecimal getImporteTotal() {
    return importeTotal;
}

public estadoReserva getEstado() {
    return estado;
}

public void setFechaInicio(LocalDateTime fechaInicio) {
    this.fechaInicio = fechaInicio;
}

public void setFechaFin(LocalDateTime fechaFin) {
    this.fechaFin = fechaFin;
}

public void setCliente(Cliente cliente) {
    this.cliente = cliente;
}

public void setVehiculo(Vehiculo vehiculo) {
    this.vehiculo = vehiculo;
}

public void setPrecioDiario(BigDecimal precioDiario) {
    this.precioDiario = precioDiario;
}

public void setImporteTotal(BigDecimal importeTotal) {
    this.importeTotal = importeTotal;
}

public void setEstado(estadoReserva estado) {
    this.estado = estado;
}
 


}
