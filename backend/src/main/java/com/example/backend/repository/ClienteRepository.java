package com.example.backend.repository;
import com.example.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
@Query("SELECT c FROM Cliente c WHERE c.idCliente = :id AND c.activo = true")
    Optional<Cliente> findByIdAndActivoTrue(@Param("id") Long id);

    boolean existsByDocumento(Long documento);

    boolean existsByEmail(String email);

    List<Cliente> findByActivoTrue();
}