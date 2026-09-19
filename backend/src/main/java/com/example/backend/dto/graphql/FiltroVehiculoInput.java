package com.example.backend.dto.graphql;

import com.example.backend.model.enums.tipoVehiculo;

public class FiltroVehiculoInput {
    private tipoVehiculo tipoVehiculo;
    private String marca;
    private String modelo;
    private Double precioMin;
    private Double precioMax;

    public FiltroVehiculoInput() {}

    public tipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(tipoVehiculo tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Double getPrecioMin() { return precioMin; }
    public void setPrecioMin(Double precioMin) { this.precioMin = precioMin; }

    public Double getPrecioMax() { return precioMax; }
    public void setPrecioMax(Double precioMax) { this.precioMax = precioMax; }
}