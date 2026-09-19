package com.example.backend.service;

import com.example.backend.dto.request.ClienteCreacionDTO;
import com.example.backend.dto.request.ClienteModificacionDTO;
import com.example.backend.dto.response.ClienteResponseDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.RecursoNoEncontradoException;
import com.example.backend.model.Cliente;
import com.example.backend.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // 1. ALTA DE CLIENTE (Req. 3)
    @Transactional
    public ClienteResponseDTO registrarCliente(ClienteCreacionDTO dto) {
        // Validación de nulidad de campos obligatorios según el contrato
        if (dto.getDocumento() == null) {
            throw new BusinessException("El documento es obligatorio");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new BusinessException("El nombre es obligatorio");
        }
        if (dto.getApellido() == null || dto.getApellido().isBlank()) {
            throw new BusinessException("El apellido es obligatorio");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new BusinessException("El email es obligatorio");
        }

        // Validación de unicidad de documento y email (Req. 3)
        if (clienteRepository.existsByDocumento(dto.getDocumento())) {
            throw new BusinessException("Ya existe un cliente registrado con el documento: " + dto.getDocumento());
        }

        String emailNormalizado = dto.getEmail().toLowerCase().trim();
        if (clienteRepository.existsByEmail(emailNormalizado)) {
            throw new BusinessException("Ya existe un cliente registrado con el email: " + emailNormalizado);
        }

        // Mapeo e inicialización de la entidad
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDocumento(dto.getDocumento());
        nuevoCliente.setNombre(dto.getNombre().trim());
        nuevoCliente.setApellido(dto.getApellido().trim());
        nuevoCliente.setEmail(emailNormalizado);
        nuevoCliente.setTelefono(dto.getTelefono().toLowerCase().trim());
        nuevoCliente.setFechaNac(dto.getFechaNacimiento());
        nuevoCliente.setActivo(true); // Todo alta inicia en estado activo

        Cliente guardado = clienteRepository.save(nuevoCliente);
        return new ClienteResponseDTO(guardado);
    }

    // 2. MODIFICACIÓN DE CLIENTE (Req. 3)
    @Transactional
    public ClienteResponseDTO modificarCliente(Long id, ClienteModificacionDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));

        // Validación y control de duplicados si se rectifica el documento
        if (dto.getDocumento() != null && !dto.getDocumento().equals(cliente.getDocumento())) {
            if (clienteRepository.existsByDocumento(dto.getDocumento())) {
                throw new BusinessException("El documento " + dto.getDocumento() + " ya pertenece a otro cliente registrado");
            }
            cliente.setDocumento(dto.getDocumento());
        }

        // Validación y control de duplicados si se actualiza el email
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            String nuevoEmail = dto.getEmail().toLowerCase().trim();
            if (!cliente.getEmail().equalsIgnoreCase(nuevoEmail)) {
                if (clienteRepository.existsByEmail(nuevoEmail)) {
                    throw new BusinessException("El email " + nuevoEmail + " ya se encuentra en uso por otro cliente");
                }
                cliente.setEmail(nuevoEmail);
            }
        }

        // Actualización de campos obligatorios y opcionales
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            cliente.setNombre(dto.getNombre().trim());
        }
        if (dto.getApellido() != null && !dto.getApellido().isBlank()) {
            cliente.setApellido(dto.getApellido().trim());
        }
        
        cliente.setTelefono(dto.getTelefono().toLowerCase().trim());
        cliente.setFechaNac(dto.getFechaNacimiento());

        Cliente actualizado = clienteRepository.save(cliente);
        return new ClienteResponseDTO(actualizado);
    }

    // 3. BAJA LÓGICA (Req. 3)
    @Transactional
    public void darDeBajaCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));

        if (!cliente.getActivo()) {
            throw new BusinessException("El cliente con ID " + id + " ya se encuentra dado de baja");
        }

        cliente.setActivo(false); // Baja lógica: preserva el historial para Hito 2 y 3
        clienteRepository.save(cliente);
    }

    // 4. CONSULTA POR ID (Req. 3)
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));
        return new ClienteResponseDTO(cliente);
    }

    // 5. CONSULTA LISTADO DE ACTIVOS (Req. 3)
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarActivos() {
        return clienteRepository.findByActivoTrue().stream()
                .map(ClienteResponseDTO::new)
                .toList();
    }

    // 6. CONSULTA LISTADO TOTAL (Administración y Auditoría)
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponseDTO::new)
                .toList();
    }
}