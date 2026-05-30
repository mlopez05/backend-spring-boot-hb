package com.umg.microservicios.pacientes_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umg.microservicios.pacientes_service.model.Paciente;

import java.util.Optional;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer>{

    Optional<Paciente> findByNumeroIdentificacion(String numeroIdentificacion);

    List<Paciente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);

    boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    List<Paciente> findByRecepcionistaIdRecepcionista(Integer idRecepcionista);

    Optional<Paciente> findByEmail(String email);

    Optional<Paciente> findByEmailIgnoreCase(String email);
}