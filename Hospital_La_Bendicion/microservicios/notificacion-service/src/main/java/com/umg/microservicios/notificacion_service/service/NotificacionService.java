package com.umg.microservicios.notificacion_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umg.microservicios.notificacion_service.model.Notificacion;
import com.umg.microservicios.notificacion_service.repository.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listarPorUsuario(Integer idUsuario){
        return notificacionRepository.findByIdUsuarioOrderByFechaDesc(idUsuario);
    }
    
    public String guardar(Notificacion notificacion){
        Notificacion noti = notificacionRepository.save(notificacion);

        if(noti == null){
            return "Error al tratar de grabar la notificación";
        }

        return "Notificación grabada con éxito";
    }

    public Notificacion marcarComoLeida(Integer id){
        Notificacion notificacion = notificacionRepository.findById(id).orElseThrow(() -> new RuntimeException("La notificación con ID " + id + " no existe."));

        notificacion.setLeido(true);
        
        return notificacionRepository.save(notificacion);
    }
}
