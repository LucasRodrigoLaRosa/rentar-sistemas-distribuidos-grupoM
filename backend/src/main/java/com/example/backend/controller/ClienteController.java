package com.example.backend.controller;

import com.example.backend.dto.request.ClienteCreacionDTO;
import com.example.backend.dto.request.ClienteModificacionDTO;
import com.example.backend.dto.response.ClienteResponseDTO;
import com.example.backend.dto.response.ErrorResponseDTO;
import com.example.backend.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operaciones REST para la gestión y administración de clientes (Req. 3)")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // 1. ALTA DE CLIENTE (Req. 3)
    @Operation(summary = "Registrar un nuevo cliente", description = "Crea un cliente activo. Valida que el documento y el email sean únicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Documento o email duplicados, o datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@RequestBody ClienteCreacionDTO dto) {
        ClienteResponseDTO creado = clienteService.registrarCliente(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    // 2. MODIFICACIÓN DE CLIENTE (Req. 3)
    @Operation(summary = "Modificar un cliente existente", description = "Actualiza los datos del cliente asegurando la unicidad de email y documento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o colisión de email/documento",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> modificarCliente(
            @PathVariable Long id,
            @RequestBody ClienteModificacionDTO dto) {
        ClienteResponseDTO actualizado = clienteService.modificarCliente(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // 3. BAJA LÓGICA DE CLIENTE (Req. 3)
    @Operation(summary = "Dar de baja lógica a un cliente", description = "Pasa el cliente a inactivo para impedir nuevos alquileres, preservando el histórico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente dado de baja correctamente"),
            @ApiResponse(responseCode = "400", description = "El cliente ya se encuentra inactivo",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBajaCliente(@PathVariable Long id) {
        clienteService.darDeBajaCliente(id);
        return ResponseEntity.noContent().build();
    }

    // 4. CONSULTA INDIVIDUAL POR ID (Req. 3)
    @Operation(summary = "Obtener un cliente por su ID", description = "Devuelve los datos de un cliente registrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente obtenido"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    // 5. CONSULTA LISTADO DE ACTIVOS (Req. 3)
    @Operation(summary = "Listar clientes activos", description = "Devuelve la lista de clientes habilitados para operar.")
    @ApiResponse(responseCode = "200", description = "Listado de clientes obtenido")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarActivos() {
        List<ClienteResponseDTO> clientes = clienteService.listarActivos();
        return ResponseEntity.ok(clientes);
    }
}