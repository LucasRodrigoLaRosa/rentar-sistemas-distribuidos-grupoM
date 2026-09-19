package com.example.backend.dto.graphql;

import com.example.backend.model.enums.tipoVehiculo;

public class VehiculoDisponibleGraphQL {
    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private tipoVehiculo tipoVehiculo;
    private Double precioDiario;

    public VehiculoDisponibleGraphQL() {}

    public VehiculoDisponibleGraphQL(Long id, String patente, String marca, String modelo, 
                                     Integer anio, String color, tipoVehiculo tipoVehiculo, Double precioDiario) {
        this.id = id;
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.tipoVehiculo = tipoVehiculo;
        this.precioDiario = precioDiario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public tipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(tipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public Double getPrecioDiario() { return precioDiario; }
    public void setPrecioDiario(Double precioDiario) { this.precioDiario = precioDiario; }
}