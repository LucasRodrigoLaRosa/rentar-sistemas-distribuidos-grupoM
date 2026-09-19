package com.example.backend.dto.graphql;

import com.example.backend.model.enums.estadoReserva;

public class ReservaGraphQL {
    private Long id;
    private String clienteNombreCompleto;
    private String vehiculoDescripcion;
    private String patente;
    private String fechaInicio;
    private String fechaFin;
    private Double precioDiario;
    private Double importeTotal;
    private estadoReserva estado;

    public ReservaGraphQL() {}

    public ReservaGraphQL(Long id, String clienteNombreCompleto, String vehiculoDescripcion, 
                          String patente, String fechaInicio, String fechaFin, 
                          Double precioDiario, Double importeTotal, estadoReserva estado) {
        this.id = id;
        this.clienteNombreCompleto = clienteNombreCompleto;
        this.vehiculoDescripcion = vehiculoDescripcion;
        this.patente = patente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioDiario = precioDiario;
        this.importeTotal = importeTotal;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteNombreCompleto() { return clienteNombreCompleto; }
    public void setClienteNombreCompleto(String clienteNombreCompleto) { this.clienteNombreCompleto = clienteNombreCompleto; }

    public String getVehiculoDescripcion() { return vehiculoDescripcion; }
    public void setVehiculoDescripcion(String vehiculoDescripcion) { this.vehiculoDescripcion = vehiculoDescripcion; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public Double getPrecioDiario() { return precioDiario; }
    public void setPrecioDiario(Double precioDiario) { this.precioDiario = precioDiario; }

    public Double getImporteTotal() { return importeTotal; }
    public void setImporteTotal(Double importeTotal) { this.importeTotal = importeTotal; }

    public estadoReserva getEstado() { return estado; }
    public void setEstado(estadoReserva estado) { this.estado = estado; }
}