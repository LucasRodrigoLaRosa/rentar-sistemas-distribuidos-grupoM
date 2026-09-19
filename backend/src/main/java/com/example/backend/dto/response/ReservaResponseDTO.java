package com.example.backend.dto.response;

import com.example.backend.model.Reserva;
import com.example.backend.model.enums.estadoReserva;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservaResponseDTO {
    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private Long vehiculoId;
    private String patenteVehiculo;
    private String modeloVehiculo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal precioDiario;
    private BigDecimal importeTotal;
    private estadoReserva estado;

    public ReservaResponseDTO() {}

    public ReservaResponseDTO(Reserva r) {
        this.id = r.getId();
        this.clienteId = r.getCliente().getId();
        this.nombreCliente = r.getCliente().getNombre() + " " + r.getCliente().getApellido();
        this.vehiculoId = r.getVehiculo().getId();
        this.patenteVehiculo = r.getVehiculo().getPatente();
        this.modeloVehiculo = r.getVehiculo().getMarca() + " " + r.getVehiculo().getModelo();
        this.fechaInicio = r.getFechaInicio();
        this.fechaFin = r.getFechaFin();
        this.precioDiario = r.getPrecioDiario();
        this.importeTotal = r.getImporteTotal();
        this.estado = r.getEstado();
    }

    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public String getNombreCliente() { return nombreCliente; }
    public Long getVehiculoId() { return vehiculoId; }
    public String getPatenteVehiculo() { return patenteVehiculo; }
    public String getModeloVehiculo() { return modeloVehiculo; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public BigDecimal getPrecioDiario() { return precioDiario; }
    public BigDecimal getImporteTotal() { return importeTotal; }
    public estadoReserva getEstado() { return estado; }
}