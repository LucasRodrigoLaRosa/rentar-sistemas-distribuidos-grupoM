package com.example.backend.service;

import com.example.backend.dto.graphql.FiltroVehiculoInput;
import com.example.backend.dto.graphql.VehiculoDisponibleGraphQL;
import com.example.backend.dto.request.VehiculoCreacionDTO;
import com.example.backend.dto.request.VehiculoModificacionDTO;
import com.example.backend.dto.response.VehiculoResponseDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.RecursoNoEncontradoException;
import com.example.backend.model.Vehiculo;
import com.example.backend.model.enums.estadoVehiculo;
import com.example.backend.repository.VehiculoRepository;
import com.example.backend.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ReservaRepository reservaRepository;
    public VehiculoService(VehiculoRepository vehiculoRepository, ReservaRepository reservaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.reservaRepository = reservaRepository;
    }

    // ALTA: Estado inicial DISPONIBLE y activo = true (Req. 1)[cite: 1]
    @Transactional
    public VehiculoResponseDTO registrarVehiculo(VehiculoCreacionDTO dto) {
        if (vehiculoRepository.existsByPatente(dto.getPatente())) {
            throw new BusinessException("Ya existe un vehículo registrado con la patente: " + dto.getPatente());
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente(dto.getPatente().toUpperCase().trim());//[cite: 1]
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setColor(dto.getColor());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setPrecioDiario(dto.getPrecioDiario());
        vehiculo.setEstado(estadoVehiculo.DISPONIBLE); // Regla Req. 1[cite: 1]
        vehiculo.setActivo(true);

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return new VehiculoResponseDTO(guardado);
    }

    // MODIFICACIÓN: La patente no se toca (Req. 1)[cite: 1]
    @Transactional
    public VehiculoResponseDTO modificarVehiculo(Long id, VehiculoModificacionDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo con ID " + id + " no encontrado"));

        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setColor(dto.getColor());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setPrecioDiario(dto.getPrecioDiario());

        Vehiculo actualizado = vehiculoRepository.save(vehiculo);
        return new VehiculoResponseDTO(actualizado);
    }

    // BAJA LÓGICA: No se elimina de la base de datos (Req. 1)[cite: 1]
    @Transactional
    public void darDeBajaVehiculo(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo con ID " + id + " no encontrado"));

        vehiculo.setActivo(false); // Baja lógica aplicada[cite: 1]
        vehiculoRepository.save(vehiculo);
    }

    // CONSULTA POR ID: Exclusivo para activos o catálogo general[cite: 1]
    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo con ID " + id + " no encontrado"));
        return new VehiculoResponseDTO(vehiculo);
    }

    // CONSULTA LISTADO: Flota activa para administración (Req. 1)[cite: 1]
    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listarActivos() {
        return vehiculoRepository.findByActivoTrue().stream()
                .map(VehiculoResponseDTO::new)
                .toList();
    }

    //--------------------------------GRAPHQL-----------------------------------------------------------------------
@Transactional(readOnly = true)
public List<VehiculoDisponibleGraphQL> obtenerDisponibles(LocalDateTime inicio, LocalDateTime fin, FiltroVehiculoInput filtro) {
    if (inicio.isBefore(LocalDateTime.now())) {
        throw new BusinessException("La fecha y hora de inicio debe ser futura.");
    }
    if (!fin.isAfter(inicio)) {
        throw new BusinessException("La fecha de finalización debe ser posterior a la fecha de inicio.");
    }

    // 1. Vehículos activos en flota
    List<Vehiculo> activos = vehiculoRepository.findByActivoTrue();

    // 2. Filtramos descartando solapamientos usando TU método JPQL
    return activos.stream()
            .filter(vehiculo -> !reservaRepository.existeSolapamiento(vehiculo.getIdVehiculo(), inicio, fin))
            // 3. Filtros dinámicos opcionales (Req. 2)
            .filter(vehiculo -> {
                if (filtro == null) return true;
                if (filtro.getTipoVehiculo() != null && vehiculo.getTipo() != filtro.getTipoVehiculo()) {
                    return false;
                }
                if (filtro.getMarca() != null && !filtro.getMarca().isBlank() && 
                    !vehiculo.getMarca().toLowerCase().contains(filtro.getMarca().toLowerCase())) {
                    return false;
                }
                if (filtro.getModelo() != null && !filtro.getModelo().isBlank() && 
                    !vehiculo.getModelo().toLowerCase().contains(filtro.getModelo().toLowerCase())) {
                    return false;
                }
                if (filtro.getPrecioMin() != null && vehiculo.getPrecioDiario().doubleValue() < filtro.getPrecioMin()) {
                    return false;
                }
                if (filtro.getPrecioMax() != null && vehiculo.getPrecioDiario().doubleValue() > filtro.getPrecioMax()) {
                    return false;
                }
                return true;
            })
            // 4. Mapeo a salida GraphQL
            .map(v -> new VehiculoDisponibleGraphQL(
                    v.getIdVehiculo(),
                    v.getPatente(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getAnio(),
                    v.getColor(),
                    v.getTipo(),
                    v.getPrecioDiario().doubleValue()
            ))
            .collect(Collectors.toList());
}



}