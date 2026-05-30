package com.umg.microservicios.citas_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;
    private String especialidad;
    private String medicoAsignado;
    private String estado; // "programada", "completada", "cancelada"

    @Column(name = "paciente_id")
    private Long pacienteId;

    @Column(name = "slot_id")
    private String slotId; // el ID que vincula con el ms-medicos
}