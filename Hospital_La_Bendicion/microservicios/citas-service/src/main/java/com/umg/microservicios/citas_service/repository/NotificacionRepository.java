package com.umg.microservicios.citas_service.repository;

import com.umg.microservicios.citas_service.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByPacienteId(Long pacienteId);
}