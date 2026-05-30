package com.umg.microservicios.citas_service.service;

import com.umg.microservicios.citas_service.model.Notificacion;
import com.umg.microservicios.citas_service.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public void enviarNotificacionReprogramacion(Long pacienteId, LocalDateTime nuevaFecha) {
        Notificacion noti = new Notificacion();
        noti.setPacienteId(pacienteId);
        noti.setMensaje("Tu cita ha sido reprogramada exitosamente para el: " + nuevaFecha);
        noti.setFechaEnvio(LocalDateTime.now());
        noti.setTipo("REPROGRAMACION");
        
        notificacionRepository.save(noti);
        
        System.out.println("LOG: Notificación enviada al paciente " + pacienteId);
    }
}