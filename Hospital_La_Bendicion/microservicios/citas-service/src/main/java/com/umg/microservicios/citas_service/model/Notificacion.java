package com.umg.microservicios.citas_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pacienteId;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String tipo; // "REPROGRAMACION", "CONFIRMACION", "RECORDATORIO"
}