package com.example.backend.controller;

import com.example.backend.dto.graphql.*;
import com.example.backend.service.VehiculoService;
import com.example.backend.service.ReservaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class GraphQLController {

    private final VehiculoService vehiculoService;
    private final ReservaService reservaService;

    public GraphQLController(VehiculoService vehiculoService, ReservaService reservaService) {
        this.vehiculoService = vehiculoService;
        this.reservaService = reservaService;
    }

    // Req. 2: Consulta de disponibilidad de vehículos
    @QueryMapping
    public List<VehiculoDisponibleGraphQL> consultarDisponibilidad(
            @Argument String fechaInicio,
            @Argument String fechaFin,
            @Argument FiltroVehiculoInput filtro) {
        
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
        LocalDateTime fin = LocalDateTime.parse(fechaFin);

        return vehiculoService.obtenerDisponibles(inicio, fin, filtro);
    }

    // Req. 5: Consulta general y filtrada de reservas
    @QueryMapping
    public List<ReservaGraphQL> consultarReservas(@Argument FiltroReservaInput filtro) {
        return reservaService.consultarReservasGraphQL(filtro);
    }

    // Req. 7: Historial de alquileres para el cliente
    @QueryMapping
    public List<HistorialAlquilerGraphQL> consultarHistorialCliente(@Argument Long clienteId) {
        return reservaService.obtenerHistorialCliente(clienteId);
    }
}