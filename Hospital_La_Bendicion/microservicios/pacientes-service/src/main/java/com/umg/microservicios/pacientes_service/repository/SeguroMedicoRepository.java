package com.umg.microservicios.pacientes_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umg.microservicios.pacientes_service.model.SeguroMedico;

import java.util.Optional;

@Repository
public interface SeguroMedicoRepository extends JpaRepository<SeguroMedico, Integer>{
    // Buscar seguro por nÃºmero de pÃ³liza
    Optional<SeguroMedico> findByNumeroPoliza(String numeroPoliza);

    // Verificar si existe una pÃ³liza
    boolean existsByNumeroPoliza(String numeroPoliza);

}
