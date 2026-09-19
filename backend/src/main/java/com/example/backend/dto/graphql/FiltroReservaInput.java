package com.example.backend.dto.graphql;

import com.example.backend.model.enums.estadoReserva;
import com.example.backend.model.enums.tipoVehiculo;

public class FiltroReservaInput {
    private Long clienteId;
    private Long vehiculoId;
    private tipoVehiculo tipoVehiculo;
    private estadoReserva estado;
    private String fechaDesde;
    private String fechaHasta;

    public FiltroReservaInput() {}

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }

    public tipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(tipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public estadoReserva getEstado() { return estado; }
    public void setEstado(estadoReserva estado) { this.estado = estado; }

    public String getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(String fechaDesde) { this.fechaDesde = fechaDesde; }

    public String getFechaHasta() { return fechaHasta; }
    public void setFechaHasta(String fechaHasta) { this.fechaHasta = fechaHasta; }
}