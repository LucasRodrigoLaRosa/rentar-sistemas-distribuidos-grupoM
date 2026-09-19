package com.example.backend.model;
import com.example.backend.model.enums.estadoVehiculo;
import com.example.backend.model.enums.tipoVehiculo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table (name = "vehiculo")
public class Vehiculo {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idVehiculo;

@NotBlank(message = "La patente es obligatoria")
@Column(nullable = false, unique = true, updatable = false, length = 15)
private String patente;

@NotBlank(message = "La marca es obligatoria")
@Column(nullable = false, length = 60)
private String marca;

@NotBlank(message = "El modelo es obligatorio")
@Column(nullable = false, length = 60)
private String modelo;

@NotNull(message = "El año es obligatorio")
@Column(nullable = false)
private int anio;

@Column(length = 40)
private String color;

@NotNull(message = "El tipo de vehículo es obligatorio")
@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 20)
private tipoVehiculo tipo;

@NotNull
@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 20)
private estadoVehiculo estado;

@Column(nullable = false)
private Boolean activo=true;

@NotNull(message = "El precio diario es obligatorio")
@DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
@Column(nullable = false, precision = 12, scale = 2)
private BigDecimal precioDiario;



public Vehiculo(){}



public Vehiculo(String patente, String marca, String modelo, int anio, String color, tipoVehiculo tipo, BigDecimal precioDiario) {
    this.patente = patente;
    this.marca = marca;
    this.modelo = modelo;
    this.anio = anio;
    this.color = color;
    this.tipo = tipo;
    this.estado = estadoVehiculo.DISPONIBLE;
    this.activo = true;
    this.precioDiario = precioDiario;
}



public String getPatente() {
    return patente;
}



public String getMarca() {
    return marca;
}



public String getModelo() {
    return modelo;
}



public int getAnio() {
    return anio;
}



public String getColor() {
    return color;
}



public tipoVehiculo getTipo() {
    return tipo;
}



public estadoVehiculo getEstado() {
    return estado;
}



public Boolean getActivo() {
    return activo;
}



public BigDecimal getPrecioDiario() {
    return precioDiario;
}



public void setPatente(String patente) {
    this.patente = patente;
}



public void setMarca(String marca) {
    this.marca = marca;
}



public void setModelo(String modelo) {
    this.modelo = modelo;
}



public void setAnio(int anio) {
    this.anio = anio;
}



public void setColor(String color) {
    this.color = color;
}



public void setTipo(tipoVehiculo tipo) {
    this.tipo = tipo;
}



public void setEstado(estadoVehiculo estado) {
    this.estado = estado;
}



public void setActivo(Boolean activo) {
    this.activo = activo;
}



public void setPrecioDiario(BigDecimal precioDiario) {
    this.precioDiario = precioDiario;
}



public Long getIdVehiculo() {
    return idVehiculo;
}






}
