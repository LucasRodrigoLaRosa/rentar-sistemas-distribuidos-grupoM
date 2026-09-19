package com.example.backend.dto.request;

import java.time.LocalDate;

public class ClienteCreacionDTO {
    private Long documento;
    private String nombre;
    private String apellido;
    private String email;
    private Long telefono;
    private LocalDate fechaNacimiento;

    // Obligatorio para la deserialización de Jackson (JSON -> Objeto)
    public ClienteCreacionDTO() {}

    public ClienteCreacionDTO(Long documento, String nombre, String apellido, 
                              String email, Long telefono, LocalDate fechaNacimiento) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getDocumento() { return documento; }
    public void setDocumento(Long documento) { this.documento = documento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}