package com.example.backend.dto.response;

import com.example.backend.model.Vehiculo;
import com.example.backend.model.enums.estadoVehiculo;
import com.example.backend.model.enums.tipoVehiculo;
import java.math.BigDecimal;

public class VehiculoResponseDTO {
    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private tipoVehiculo tipo;
    private BigDecimal precioDiario;
    private estadoVehiculo estado;
    private Boolean activo;

    public VehiculoResponseDTO() {}

    public VehiculoResponseDTO(Vehiculo v) {
        this.id = v.getIdVehiculo();
        this.patente = v.getPatente();
        this.marca = v.getMarca();
        this.modelo = v.getModelo();
        this.anio = v.getAnio();
        this.color = v.getColor();
        this.tipo = v.getTipo();
        this.precioDiario = v.getPrecioDiario();
        this.estado = v.getEstado();
        this.activo = v.getActivo();
    }

    public Long getId() { return id; }
    public String getPatente() { return patente; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public Integer getAnio() { return anio; }
    public String getColor() { return color; }
    public tipoVehiculo getTipo() { return tipo; }
    public BigDecimal getPrecioDiario() { return precioDiario; }
    public estadoVehiculo getEstado() { return estado; }
    public Boolean getActivo() { return activo; }
}
