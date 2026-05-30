package com.umg.microservicios.citas_service.scheduler;

import com.umg.microservicios.citas_service.config.RabbitMQConfig;
import com.umg.microservicios.citas_service.model.Cita;
import com.umg.microservicios.citas_service.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordatorioCronJob {

    private final CitaRepository citaRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0 * * * *")
    public void enviarRecordatorios() {
        LocalDateTime ahora  = LocalDateTime.now();
        LocalDateTime desde  = ahora.plusHours(24);
        LocalDateTime hasta  = ahora.plusHours(25);

        log.info("CronJob recordatorios ejecutado a {}. Buscando citas entre {} y {}",
                ahora, desde, hasta);

        List<Cita> citas = citaRepository.findCitasParaRecordatorio(desde, hasta);

        if (citas.isEmpty()) {
            log.info("No hay citas para recordar en esta ventana horaria.");
            return;
        }

        log.info("Encontradas {} cita(s) para enviar recordatorio.", citas.size());

        for (Cita cita : citas) {
            try {
                Map<String, Object> evento = new HashMap<>();
                evento.put("paciente_id",  cita.getPacienteId());
                evento.put("id_cita",      cita.getId());
                evento.put("especialidad", cita.getEspecialidad());
                evento.put("medico",       cita.getMedicoAsignado());
                evento.put("fecha_hora",   cita.getFechaHora().toString());
                evento.put("timestamp",    ahora.toString());

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE,
                        RabbitMQConfig.RK_CITA_RECORDATORIO,
                        evento
                );

                log.info("Recordatorio publicado para cita {} del paciente {}",
                        cita.getId(), cita.getPacienteId());

            } catch (Exception e) {
                log.error("Error al publicar recordatorio para cita {}: {}",
                        cita.getId(), e.getMessage());
            }
        }
    }
}