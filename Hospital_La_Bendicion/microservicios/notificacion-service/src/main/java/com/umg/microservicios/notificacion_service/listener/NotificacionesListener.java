package com.umg.microservicios.notificacion_service.listener;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.umg.microservicios.notificacion_service.client.PacientesClient;
import com.umg.microservicios.notificacion_service.model.Notificacion;
import com.umg.microservicios.notificacion_service.repository.NotificacionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificacionesListener {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private PacientesClient pacientesClient;

    @RabbitListener(queues = "cola.notificaciones.general")
    public void procesarEventosEcosistema(Map<String, Object> mensaje,
                                          @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {

        log.info("Evento recibido - routing key: {}", routingKey);

        String correoDestino = (String) mensaje.get("email");
        Integer idUsuario    = toInteger(mensaje.get("paciente_id"));

        String titulo        = "";
        String cuerpoMensaje = "";

        switch (routingKey) {

            case "medicos.creado":
                titulo = "¡Bienvenido al Cuerpo Médico!";
                cuerpoMensaje = "Estimado/a Dr. " + mensaje.get("apellidos")
                        + ",\n\nSu cuenta de especialista ha sido activada exitosamente.\n"
                        + "Su contraseña temporal es: 1234\n"
                        + "Por favor cámbiela en su próximo inicio de sesión.";
                idUsuario     = toInteger(mensaje.get("idUsuario"));
                correoDestino = (String) mensaje.get("email");
                break;

            case "cita.confirmada":
                titulo = "Cita Confirmada";
                cuerpoMensaje = "Su cita de " + mensaje.get("especialidad")
                        + " con " + mensaje.get("medico")
                        + " ha sido confirmada para el " + mensaje.get("fecha_hora") + ".";
                break;

            case "cita.reprogramada":
                titulo = "Cita Reprogramada";
                cuerpoMensaje = "Su cita de " + mensaje.get("especialidad")
                        + " ha sido reprogramada para el " + mensaje.get("nueva_fecha_hora") + ".";
                break;

            case "cita.cancelada":
                titulo = "Cita Cancelada";
                String motivo = (String) mensaje.get("motivo");
                cuerpoMensaje = "Su cita de " + mensaje.get("especialidad") + " ha sido cancelada."
                        + (motivo != null && !motivo.isBlank() ? " Motivo: " + motivo : "");
                break;

            // El CronJob solo manda paciente_id, sin email — hay que resolverlo
            case "cita.recordatorio":
                titulo = "Recordatorio de Cita";
                cuerpoMensaje = "Recuerde que tiene una cita de " + mensaje.get("especialidad")
                        + " con " + mensaje.get("medico")
                        + " mañana a las " + mensaje.get("fecha_hora") + "."
                        + "\n\nPor favor llegue con 15 minutos de anticipación.";
                correoDestino = resolverEmailPaciente(idUsuario);
                break;

            default:
                titulo = "Alerta del Sistema";
                cuerpoMensaje = "Tiene una nueva notificación en su panel de control.";
                break;
        }

        // ── Guardar en BD (alimenta la campanita) ─────────────────────────
        if (idUsuario != null) {
            try {
                Notificacion nueva = new Notificacion();
                nueva.setIdUsuario(idUsuario);
                nueva.setTitulo(titulo);
                nueva.setMensaje(cuerpoMensaje);
                nueva.setLeido(false);
                nueva.setFecha(LocalDate.now());
                notificacionRepository.save(nueva);
                log.info("Notificación guardada en BD para usuario ID: {}", idUsuario);
            } catch (Exception e) {
                log.error("No se pudo guardar la notificación en BD: {}", e.getMessage());
            }
        }

        // ── Enviar correo si hay destinatario ─────────────────────────────
        if (correoDestino != null && !correoDestino.isBlank()) {
            try {
                SimpleMailMessage correo = new SimpleMailMessage();
                correo.setTo(correoDestino);
                correo.setSubject("[Hospital La Bendición] " + titulo);
                correo.setText(cuerpoMensaje);
                mailSender.send(correo);
                log.info("Correo enviado a: {}", correoDestino);
            } catch (Exception e) {
                log.error("Error al enviar correo a {}: {}", correoDestino, e.getMessage());
                throw new RuntimeException("Fallo en el envío de correo", e);
            }
        } else {
            log.warn("Evento '{}' sin correo destino para usuario ID: {}", routingKey, idUsuario);
        }
    }

    private String resolverEmailPaciente(Integer pacienteId) {
        if (pacienteId == null) return null;
        try {
            Map<String, Object> paciente = pacientesClient.obtenerPaciente(pacienteId);
            return (String) paciente.get("email");
        } catch (Exception e) {
            log.error("No se pudo obtener email del paciente ID {}: {}", pacienteId, e.getMessage());
            return null;
        }
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        try { return Integer.parseInt(value.toString()); }
        catch (NumberFormatException e) { return null; }
    }
}