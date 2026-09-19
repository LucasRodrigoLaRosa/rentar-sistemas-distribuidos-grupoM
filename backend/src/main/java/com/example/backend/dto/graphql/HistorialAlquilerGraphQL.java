package com.example.backend.dto.graphql;

import com.example.backend.model.enums.estadoReserva;

public class HistorialAlquilerGraphQL {
    private Long id;
    private String vehiculoDescripcion;
    private String patente;
    private String fechaInicio;
    private String fechaFin;
    private Integer cantidadDias;
    private Double importeTotal;
    private estadoReserva estado;

    public HistorialAlquilerGraphQL() {}

    public HistorialAlquilerGraphQL(Long id, String vehiculoDescripcion, String patente, 
                                   String fechaInicio, String fechaFin, Integer cantidadDias, 
                                   Double importeTotal, estadoReserva estado) {
        this.id = id;
        this.vehiculoDescripcion = vehiculoDescripcion;
        this.patente = patente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadDias = cantidadDias;
        this.importeTotal = importeTotal;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVehiculoDescripcion() { return vehiculoDescripcion; }
    public void setVehiculoDescripcion(String vehiculoDescripcion) { this.vehiculoDescripcion = vehiculoDescripcion; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public Integer getCantidadDias() { return cantidadDias; }
    public void setCantidadDias(Integer cantidadDias) { this.cantidadDias = cantidadDias; }

    public Double getImporteTotal() { return importeTotal; }
    public void setImporteTotal(Double importeTotal) { this.importeTotal = importeTotal; }

    public estadoReserva getEstado() { return estado; }
    public void setEstado(estadoReserva estado) { this.estado = estado; }
}