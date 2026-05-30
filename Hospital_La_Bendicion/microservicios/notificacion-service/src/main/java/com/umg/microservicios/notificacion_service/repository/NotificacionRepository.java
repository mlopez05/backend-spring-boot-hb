package com.umg.microservicios.notificacion_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umg.microservicios.notificacion_service.model.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByIdUsuarioOrderByFechaDesc(Integer idUsuario);
}
