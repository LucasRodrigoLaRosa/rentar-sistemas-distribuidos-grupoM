package com.example.backend.service;

import com.example.backend.dto.request.VehiculoCreacionDTO;
import com.example.backend.dto.request.VehiculoModificacionDTO;
import com.example.backend.dto.response.VehiculoResponseDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.RecursoNoEncontradoException;
import com.example.backend.model.Vehiculo;
import com.example.backend.model.enums.estadoVehiculo;
import com.example.backend.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    // ALTA: Estado inicial DISPONIBLE y activo = true (Req. 1)[cite: 1]
    @Transactional
    public VehiculoResponseDTO registrarVehiculo(VehiculoCreacionDTO dto) {
        if (vehiculoRepository.existsByPatente(dto.getPatente())) {
            throw new BusinessException("Ya existe un vehículo registrado con la patente: " + dto.getPatente());[cite: 1]
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
                .toList();[cite: 1]
    }
}