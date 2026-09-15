package com.example.backend.model;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="cliente")
public class Cliente {

    //Se usa Bean Validation (validacion de sintaxis) ,
    /*
    
    Fail-Fast (Fallo Temprano): Si un cliente envía una petición HTTP mal formada (por ejemplo, un JSON sin nombre o con un documento nulo
     a /api/v1/clientes), Spring intercepta la carga en la capa web mediante @Valid y corta la ejecución de inmediato devolviendo un error
    HTTP 400 (Bad Request). Esto evita desperdiciar procesamiento en la capa @Service o saturar la base de datos con transacciones inválidas.

     Defensa del Modelo de Dominio: Garantiza que los objetos mantengan su consistencia interna sin importar por qué canal ingresaron
    (sea un endpoint REST, una mutación GraphQL o un mensaje deserializado desde Kafka) 
    
    Se complementan dividiendo las responsabilidades en dos frentes: la validación de formato (sintaxis) y la validación de negocio (semántica).

    Se evalúan en memoria apenas llega el JSON al Controller.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @NotNull(message = "El documento es obligatorio")
    @Column(nullable = false, unique = true)
    private Long documento; //(obligatorio)

    @NotBlank(message="El nombre es obligatorio")
    @Column(nullable = false , lenght = 20)
    private String nombre; //(obligatorio)

    @NotBlank(message="El apellifo es obligatorio")
    @Column(nullable = false, lengt = 20)
    private String apellido; // (obligatorio)

    @NotBlank(message="El mail es obligatorio")
    @Email(message="Formato inválido")
    @Column(nullable = false, unique = true , lengt =120)
    private String email; //(único y obligatorio)

    @Column (lengt = 30)
    private Long teléfono;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(nullable = false)
    private LocalDate fechaNac;

    @Column(nullable = false)
    private Boolean activo;

public Cliente() {
}

public Cliente(Long documento, String nombre, String apellido, String email, Long teléfono, LocalDate fechaNac) {
    this.documento = documento;
    this.nombre = nombre;
    this.apellido = apellido;
    this.email = email;
    this.teléfono = teléfono;
    this.fechaNac = fechaNac;
    this.activo=true;
}

public Long getDocumento() {
    return documento;
}

public String getNombre() {
    return nombre;
}

public String getApellido() {
    return apellido;
}

public String getEmail() {
    return email;
}

public Long getTeléfono() {
    return teléfono;
}

public LocalDate getFechaNac() {
    return fechaNac;
}

public Boolean getActivo() {
    return activo;
}

public void setDocumento(Long documento) {
    this.documento = documento;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public void setApellido(String apellido) {
    this.apellido = apellido;
}

public void setEmail(String email) {
    this.email = email;
}

public void setTeléfono(Long teléfono) {
    this.teléfono = teléfono;
}

public void setFechaNac(LocalDate fechaNac) {
    this.fechaNac = fechaNac;
}

public void setActivo(Boolean activo) {
    this.activo = activo;
}




}
