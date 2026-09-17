package com.example.backend.dto.request;

import com.example.backend.model.enums.tipoVehiculo;
import java.math.BigDecimal;

public class VehiculoCreacionDTO {
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private tipoVehiculo tipo;
    private BigDecimal precioDiario;

    public VehiculoCreacionDTO() {}

    
    public VehiculoCreacionDTO(String patente, String marca, String modelo, Integer anio, String color,
            tipoVehiculo tipo, BigDecimal precioDiario) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.tipo = tipo;
        this.precioDiario = precioDiario;
    }


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

    public tipoVehiculo getTipo() { return tipo; }
    public void setTipo(tipoVehiculo tipo) { this.tipo = tipo; }

    public BigDecimal getPrecioDiario() { return precioDiario; }
    public void setPrecioDiario(BigDecimal precioDiario) { this.precioDiario = precioDiario; }
}