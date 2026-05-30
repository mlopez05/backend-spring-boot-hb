package com.umg.microservicios.medicos_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umg.microservicios.medicos_service.model.SlotAgenda;

public interface SlotAgendaRepository extends JpaRepository<SlotAgenda, Integer> {

    List<SlotAgenda> findByMedicoIdAndEstadoAndFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(Integer medicoId, String estado, LocalDate desde);
 
    List<SlotAgenda> findByMedicoIdAndFecha(Integer medicoId, LocalDate fecha);
}
