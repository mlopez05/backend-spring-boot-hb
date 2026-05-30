package com.umg.microservicios.pacientes_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umg.microservicios.pacientes_service.model.Recepcionista;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Integer> {
// Buscar recepcionista por email
    java.util.Optional<Recepcionista> findByEmail(String email);

}
