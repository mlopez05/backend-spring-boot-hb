package com.umg.microservicios.citas_service.service;
 
import com.umg.microservicios.citas_service.config.JwtUtil;
import com.umg.microservicios.citas_service.config.RabbitMQConfig;
import com.umg.microservicios.citas_service.dto.CitasResponseDTO;
import com.umg.microservicios.citas_service.dto.CancelarCitaRequest;
import com.umg.microservicios.citas_service.dto.ReprogramarCitaRequest;
import com.umg.microservicios.citas_service.dto.SolicitarCitaRequest;
import com.umg.microservicios.citas_service.exception.ConflictException;
import com.umg.microservicios.citas_service.exception.PolicyException;
import com.umg.microservicios.citas_service.model.Cita;
import com.umg.microservicios.citas_service.repository.CitaRepository;
import com.umg.microservicios.citas_service.repository.MedicosClient;
import com.umg.microservicios.citas_service.repository.PacientesClient;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
 
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitaService {
 
    private final CitaRepository repository;
    private final MedicosClient medicosClient;
    private final PacientesClient pacientesClient;
    private final RabbitTemplate rabbitTemplate;
    private final JwtUtil jwtUtil;

    public Long resolverPacienteId(String authHeader) {
    String email = jwtUtil.getUsernameFromToken(authHeader);
    Map<String, Object> paciente = pacientesClient.buscarPorEmail(email);
    if (paciente == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No se encontró paciente asociado al usuario: " + email);
    }
    Object idObj = paciente.get("idPaciente");
    if (idObj == null) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "No se pudo determinar el ID del paciente.");
    }
    return Long.valueOf(idObj.toString());
}
 
 
    @Transactional
    public Cita solicitarCita(SolicitarCitaRequest request, String authHeader) {
        Long pacienteId = resolverPacienteId(authHeader);
 
        try {
            medicosClient.reservarSlot(request.slotId());
        } catch (FeignException.Conflict e) {
            throw new ConflictException("El horario seleccionado ya no está disponible. Por favor elige otro.");
        }
 
        Cita cita = new Cita();
        cita.setPacienteId(pacienteId);
        cita.setSlotId(String.valueOf(request.slotId()));
        cita.setFechaHora(request.fechaHora());
        cita.setEspecialidad(request.especialidad());
        cita.setMedicoAsignado(request.medicoAsignado());
        cita.setEstado("PROGRAMADA");
 
        Cita citaGuardada = repository.save(cita);
 
        publicarEvento(RabbitMQConfig.RK_CITA_CONFIRMADA, Map.of(
                "paciente_id",    pacienteId,
                "id_cita",        citaGuardada.getId(),
                "especialidad",   citaGuardada.getEspecialidad(),
                "medico",         citaGuardada.getMedicoAsignado(),
                "fecha_hora",     citaGuardada.getFechaHora().toString()
        ));
 
        log.info("Cita {} creada para paciente {}", citaGuardada.getId(), pacienteId);
        return citaGuardada;
    }
 
 
    @Transactional
    public Cita reprogramarCita(Long idCita, ReprogramarCitaRequest request, String authHeader) {
        Long pacienteId = resolverPacienteId(authHeader);
 
        Cita cita = repository.findById(idCita)
                .orElseThrow(() -> new EntityNotFoundException(
                        "La cita solicitada no existe."));
 
        if (!cita.getPacienteId().equals(pacienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tiene permisos para modificar esta cita.");
        }
 
        validarEstadoProgamada(cita);
        validarRegla24Horas(cita);
 
        String slotViejoId = cita.getSlotId();
 
        try {
            medicosClient.reservarSlot(Integer.valueOf(request.nuevoSlotId()));
        } catch (FeignException.Conflict e) {
            throw new ConflictException("El nuevo horario ya no está disponible.");
        }
 
        cita.setSlotId(request.nuevoSlotId());
        cita.setFechaHora(request.nuevaFechaHora());
        Cita actualizada = repository.save(cita);
 
        publicarEvento(RabbitMQConfig.RK_CITA_REPROGRAMADA, Map.of(
                "paciente_id",     pacienteId,
                "id_cita",         actualizada.getId(),
                "especialidad",    actualizada.getEspecialidad(),
                "nueva_fecha_hora", actualizada.getFechaHora().toString()
        ));
 
        try {
            medicosClient.liberarSlot(Integer.valueOf(slotViejoId));
        } catch (Exception e) {
            log.warn("No se pudo liberar el slot anterior {}: {}", slotViejoId, e.getMessage());
        }
 
        return actualizada;
    }
 
    @Transactional
    public Cita cancelarCita(Long idCita, CancelarCitaRequest request, String authHeader) {
        Long pacienteId = resolverPacienteId(authHeader);
 
        Cita cita = repository.findById(idCita)
                .orElseThrow(() -> new EntityNotFoundException(
                        "La cita solicitada no existe."));
 
        if (!cita.getPacienteId().equals(pacienteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tiene permisos para cancelar esta cita.");
        }
 
        validarEstadoProgamada(cita);
        validarRegla24Horas(cita);
 
        String slotId = cita.getSlotId();
 
        cita.setEstado("CANCELADA");
        Cita cancelada = repository.save(cita);
 
        publicarEvento(RabbitMQConfig.RK_CITA_CANCELADA, Map.of(
                "paciente_id",  pacienteId,
                "id_cita",      cancelada.getId(),
                "especialidad", cancelada.getEspecialidad(),
                "motivo",       request != null && request.motivo() != null ? request.motivo() : ""
        ));
 
        try {
            medicosClient.liberarSlot(Integer.valueOf(slotId));
        } catch (Exception e) {
            log.warn("No se pudo liberar el slot {} al cancelar cita {}: {}", slotId, idCita, e.getMessage());
        }
 
        log.info("Cita {} cancelada por paciente {}", idCita, pacienteId);
        return cancelada;
    }
 
 
    public CitasResponseDTO obtenerHistorialPaciente(String authHeader) {
        Long pacienteId = resolverPacienteId(authHeader);
        List<Cita> todas = repository.findByPacienteId(pacienteId);
        LocalDateTime ahora = LocalDateTime.now();
 
        return new CitasResponseDTO(
                todas.stream().filter(c -> c.getFechaHora().isAfter(ahora)).toList(),
                todas.stream().filter(c -> c.getFechaHora().isBefore(ahora)).toList()
        );
    }
 
    public List<Cita> obtenerTodasLasCitas() {
        return repository.findAll();
    }

 
    private void validarEstadoProgamada(Cita cita) {
        if (!"PROGRAMADA".equalsIgnoreCase(cita.getEstado())) {
            throw new PolicyException(
                    "Solo se pueden modificar citas en estado PROGRAMADA. Estado actual: " + cita.getEstado());
        }
    }
 
    private void validarRegla24Horas(Cita cita) {
        long horasRestantes = Duration.between(LocalDateTime.now(), cita.getFechaHora()).toHours();
        if (horasRestantes < 24) {
            throw new PolicyException(
                    "No se pueden realizar cambios con menos de 24 horas de anticipación.");
        }
    }
 
    private void publicarEvento(String routingKey, Map<String, Object> payload) {
        try {
            Map<String, Object> evento = new HashMap<>(payload);
            evento.put("timestamp", LocalDateTime.now().toString());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, routingKey, evento);
            log.info("Evento '{}' publicado en RabbitMQ", routingKey);
        } catch (Exception e) {
            log.error("No se pudo publicar evento '{}': {}", routingKey, e.getMessage());
        }
    }
}