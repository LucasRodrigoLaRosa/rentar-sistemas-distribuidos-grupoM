package com.example.backend.service;

import com.example.backend.dto.graphql.FiltroReservaInput;
import com.example.backend.dto.graphql.HistorialAlquilerGraphQL;
import com.example.backend.dto.graphql.ReservaGraphQL;
import com.example.backend.dto.request.ReservaCreacionDTO;
import com.example.backend.dto.response.ReservaResponseDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.RecursoNoEncontradoException;
import com.example.backend.exception.VehiculoNoDisponibleException;
import com.example.backend.model.Cliente;
import com.example.backend.model.Reserva;
import com.example.backend.model.Vehiculo;
import com.example.backend.model.enums.estadoReserva;
import com.example.backend.repository.ClienteRepository;
import com.example.backend.repository.ReservaRepository;
import com.example.backend.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          VehiculoRepository vehiculoRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    // ALTA DE RESERVA (Req. 4)
    @Transactional
    public ReservaResponseDTO crearReserva(ReservaCreacionDTO dto) {
        LocalDateTime ahora = LocalDateTime.now();

        // 1. Validaciones temporales de contrato
        if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
            throw new BusinessException("Las fechas de inicio y finalización son obligatorias");
        }
        if (dto.getFechaInicio().isBefore(ahora)) {
            throw new BusinessException("La fecha de inicio debe ser posterior al momento actual");
        }
        if (!dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            throw new BusinessException("La fecha de finalización debe ser posterior a la fecha de inicio");
        }

        // 2. Comprobar existencia y estado del Cliente (Req. 4)
        //********ACA HABRIA QUE PONER VALIDACIONES POR SEPRADFA PARA SABER SI NO SE ENCONTRO O NO ESTABA ACTIVO******
        Cliente cliente = clienteRepository.findByIdAndActivoTrue(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado o inactivo con ID: " + dto.getClienteId()));

        // 3. Comprobar existencia y estado del Vehículo (Req. 4)
        Vehiculo vehiculo = vehiculoRepository.findByIdAndActivoTrue(dto.getVehiculoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Vehículo no encontrado o inactivo con ID: " + dto.getVehiculoId()));
        //------------------------------------------------------------------------------------------------------
        
        
        // 4. Verificación de solapamiento temporal en la base de datos (Req. 4)
        boolean solapado = reservaRepository.existeSolapamiento(
                vehiculo.getIdVehiculo(),
                dto.getFechaInicio(),
                dto.getFechaFin()
        );

        if (solapado) {
            throw new VehiculoNoDisponibleException(
                    "El vehículo patente " + vehiculo.getPatente() + " ya cuenta con una reserva en ese rango horario");
        }

        // 5. Cálculo del importe total (fracción de día cuenta como 1 día completo)
        long horas = Duration.between(dto.getFechaInicio(), dto.getFechaFin()).toHours();
        long dias = (long) Math.ceil((double) horas / 24.0);
        if (dias == 0) {
            dias = 1;
        }

        BigDecimal importeTotal = vehiculo.getPrecioDiario().multiply(BigDecimal.valueOf(dias));

        // 6. Persistencia de la entidad
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setVehiculo(vehiculo);
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setPrecioDiario(vehiculo.getPrecioDiario());
        reserva.setImporteTotal(importeTotal);
        reserva.setEstado(estadoReserva.CONFIRMADA); // Req. 4

        Reserva guardada = reservaRepository.save(reserva);
        return new ReservaResponseDTO(guardada);
    }

    // CANCELACIÓN DE RESERVA (Req. 6)
    @Transactional
    public ReservaResponseDTO cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada con ID: " + reservaId));

        // Regla: Solo si el período todavía no comenzó (Req. 6)
        if (LocalDateTime.now().isAfter(reserva.getFechaInicio())) {
            throw new BusinessException("No se puede cancelar una reserva cuyo período de alquiler ya ha comenzado");
        }

        if (reserva.getEstado() == estadoReserva.CANCELADA) {
            throw new BusinessException("La reserva ya se encuentra cancelada");
        }

        // Baja lógica: no se borra físicamente, pasa a CANCELADA (Req. 6)
        reserva.setEstado(estadoReserva.CANCELADA);
        Reserva cancelada = reservaRepository.save(reserva);
        return new ReservaResponseDTO(cancelada);
    }

    // CONSULTA INDIVIDUAL POR ID (Soporte REST)
    @Transactional(readOnly = true)
    public ReservaResponseDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada con ID: " + id));
        return new ReservaResponseDTO(reserva);
    }

    // CONSULTA LISTADO TOTAL (Administración REST)
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listarTodas() {
        return reservaRepository.findAll().stream()
                .map(ReservaResponseDTO::new)
                .toList();
    }

    //----------------------GRAPQHL---------------------------------------------

    @Transactional(readOnly = true)
public List<HistorialAlquilerGraphQL> obtenerHistorialCliente(Long clienteId) {
    if (!clienteRepository.existsById(clienteId)) {
        throw new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId);
    }

    // Usamos el método exacto que ya tenés en tu interfaz:
    List<Reserva> reservas = reservaRepository.findByClienteIdAndEstadoIn(
            clienteId, 
            List.of(estadoReserva.FINALIZADA, estadoReserva.CANCELADA)
    );

    return reservas.stream()
            .map(r -> {
                long dias = ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin());
                int cantidadDias = (int) Math.max(1, dias);

                return new HistorialAlquilerGraphQL(
                        r.getIdAlquiler(),
                        r.getVehiculo().getMarca() + " " + r.getVehiculo().getModelo(),
                        r.getVehiculo().getPatente(),
                        r.getFechaInicio().toString(),
                        r.getFechaFin().toString(),
                        cantidadDias,
                        r.getImporteTotal().doubleValue(),
                        r.getEstado()
                );
            })
            .collect(Collectors.toList());
}

@Transactional(readOnly = true)
public List<ReservaGraphQL> consultarReservasGraphQL(FiltroReservaInput filtro) {
    List<Reserva> reservas = reservaRepository.findAll();

    return reservas.stream()
            .filter(r -> {
                if (filtro == null) return true;
                if (filtro.getClienteId() != null && !r.getCliente().getIdCliente().equals(filtro.getClienteId())) {
                    return false;
                }
                if (filtro.getVehiculoId() != null && !r.getVehiculo().getIdVehiculo().equals(filtro.getVehiculoId())) {
                    return false;
                }
                if (filtro.getTipoVehiculo() != null && r.getVehiculo().getTipo() != filtro.getTipoVehiculo()) {
                    return false;
                }
                if (filtro.getEstado() != null && r.getEstado() != filtro.getEstado()) {
                    return false;
                }
                if (filtro.getFechaDesde() != null && !filtro.getFechaDesde().isBlank()) {
                    LocalDateTime desde = LocalDateTime.parse(filtro.getFechaDesde());
                    if (r.getFechaInicio().isBefore(desde)) return false;
                }
                if (filtro.getFechaHasta() != null && !filtro.getFechaHasta().isBlank()) {
                    LocalDateTime hasta = LocalDateTime.parse(filtro.getFechaHasta());
                    if (r.getFechaFin().isAfter(hasta)) return false;
                }
                return true;
            })
            .map(r -> new ReservaGraphQL(
                    r.getIdAlquiler(),
                    r.getCliente().getNombre() + " " + r.getCliente().getApellido(),
                    r.getVehiculo().getMarca() + " " + r.getVehiculo().getModelo(),
                    r.getVehiculo().getPatente(),
                    r.getFechaInicio().toString(),
                    r.getFechaFin().toString(),
                    r.getVehiculo().getPrecioDiario().doubleValue(),
                    r.getImporteTotal().doubleValue(),
                    r.getEstado()
            ))
            .collect(Collectors.toList());
}

}