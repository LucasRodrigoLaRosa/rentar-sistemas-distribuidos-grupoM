package com.example.backend.dto.response;

import com.example.backend.model.Cliente;
import java.time.LocalDate;

public class ClienteResponseDTO {
    private Long id;
    private Long documento;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private Boolean activo;

    public ClienteResponseDTO() {}

    // Constructor mapper: extrae los datos de la entidad persistida
    public ClienteResponseDTO(Cliente c) {
        this.id = c.getIdCliente();
        this.documento = c.getDocumento();
        this.nombre = c.getNombre();
        this.apellido = c.getApellido();
        this.email = c.getEmail();
        this.telefono = c.getTelefono();
        this.fechaNacimiento = c.getFechaNac();
        this.activo = c.getActivo();
    }

    public Long getId() { return id; }
    public Long getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public Boolean getActivo() { return activo; }
}