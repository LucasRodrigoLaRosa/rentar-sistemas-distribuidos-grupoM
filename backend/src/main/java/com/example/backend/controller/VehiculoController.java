package com.example.backend.controller;

import com.example.backend.dto.request.VehiculoCreacionDTO;
import com.example.backend.dto.request.VehiculoModificacionDTO;
import com.example.backend.dto.response.ErrorResponseDTO;
import com.example.backend.dto.response.VehiculoResponseDTO;
import com.example.backend.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos", description = "Endpoints REST para el ABM y gestión de la flota de Rentar (Req. 1)")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    // 1. ALTA DE VEHÍCULO (Req. 1)
    @Operation(summary = "Registrar un nuevo vehículo", description = "Crea un vehículo en estado inicial DISPONIBLE y activo. La patente debe ser única.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehículo registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Patente duplicada o datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<VehiculoResponseDTO> registrarVehiculo(@Valid @RequestBody VehiculoCreacionDTO dto) {
        VehiculoResponseDTO creado = vehiculoService.registrarVehiculo(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    // 2. MODIFICACIÓN DE VEHÍCULO (Req. 1)
    @Operation(summary = "Modificar un vehículo existente", description = "Actualiza atributos del vehículo. La patente no puede modificarse una vez registrada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo modificado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos erróneos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> modificarVehiculo(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoModificacionDTO dto) {
        VehiculoResponseDTO actualizado = vehiculoService.modificarVehiculo(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // 3. BAJA LÓGICA DE VEHÍCULO (Req. 1)
    @Operation(summary = "Dar de baja lógica un vehículo", description = "Pasa el vehículo a inactivo impidiendo nuevos alquileres y preservando su histórico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehículo dado de baja lógicamente"),
            @ApiResponse(responseCode = "400", description = "El vehículo ya se encuentra inactivo",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBajaVehiculo(@PathVariable Long id) {
        vehiculoService.darDeBajaVehiculo(id);
        return ResponseEntity.noContent().build();
    }

    // 4. CONSULTA INDIVIDUAL POR ID (Req. 1)
    @Operation(summary = "Obtener vehículo por ID", description = "Devuelve el detalle de un vehículo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorId(@PathVariable Long id) {
        VehiculoResponseDTO vehiculo = vehiculoService.obtenerPorId(id);
        return ResponseEntity.ok(vehiculo);
    }

    // 5. LISTADO DE VEHÍCULOS ACTIVOS (Req. 1)
    @Operation(summary = "Listar vehículos activos", description = "Devuelve la flota activa disponible para operar.")
    @ApiResponse(responseCode = "200", description = "Listado de vehículos recuperado")
    @GetMapping
    public ResponseEntity<List<VehiculoResponseDTO>> listarActivos() {
        List<VehiculoResponseDTO> activos = vehiculoService.listarActivos();
        return ResponseEntity.ok(activos);
    }
}
