package com.example.backend.dto.request;

import java.time.LocalDateTime;

public class ReservaCreacionDTO {
    private Long clienteId;
    private Long vehiculoId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    // Obligatorio para Jackson
    public ReservaCreacionDTO() {}

    public ReservaCreacionDTO(Long clienteId, Long vehiculoId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.clienteId = clienteId;
        this.vehiculoId = vehiculoId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
}