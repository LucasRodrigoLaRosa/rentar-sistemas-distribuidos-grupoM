package com.example.backend.controller;

import com.example.backend.dto.request.ReservaCreacionDTO;
import com.example.backend.dto.response.ErrorResponseDTO;
import com.example.backend.dto.response.ReservaResponseDTO;
import com.example.backend.service.ReservaService;
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
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Endpoints transaccionales para altas y cancelaciones de reservas (Req. 4 y Req. 6)")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // 1. ALTA DE RESERVA (Req. 4)
    @Operation(summary = "Crear una nueva reserva", 
               description = "Registra una reserva en estado CONFIRMADA tras verificar existencia y estado activo de cliente y vehículo, coherencia temporal e inexistencia de solapamiento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva confirmada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas o parámetros incorrectos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente o Vehículo no encontrado o inactivo",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: Vehículo no disponible en ese rango temporal (Solapamiento detectado)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(@Valid @RequestBody ReservaCreacionDTO dto) {
        ReservaResponseDTO creada = reservaService.crearReserva(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity.created(location).body(creada);
    }

    // 2. CANCELACIÓN DE RESERVA (Req. 6)
    @Operation(summary = "Cancelar una reserva existente", 
               description = "Pasa el estado a CANCELADA sin borrado físico en base de datos. Solo se permite si el período de alquiler aún no ha comenzado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "400", description = "El período de alquiler ya inició o la reserva ya estaba cancelada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Long id) {
        ReservaResponseDTO cancelada = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(cancelada);
    }

    // 3. CONSULTA DE RESERVA POR ID (Auditoría REST)
    @Operation(summary = "Consultar reserva por ID", description = "Devuelve los datos detallados de una reserva registrada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long id) {
        ReservaResponseDTO reserva = reservaService.obtenerPorId(id);
        return ResponseEntity.ok(reserva);
    }

    // 4. LISTADO GENERAL DE RESERVAS
    @Operation(summary = "Listar todas las reservas", description = "Obtiene el listado completo de reservas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Listado de reservas obtenido")
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        List<ReservaResponseDTO> reservas = reservaService.listarTodas();
        return ResponseEntity.ok(reservas);
    }
}