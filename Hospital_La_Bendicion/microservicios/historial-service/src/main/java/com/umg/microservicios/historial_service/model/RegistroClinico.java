package com.umg.microservicios.historial_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.time.LocalDateTime;

@Entity
@Table(name = "registros_clinicos", indexes = {
        @Index(name = "idx_rc_paciente", columnList = "paciente_id"),
        @Index(name = "idx_rc_medico",   columnList = "medico_id"),
        @Index(name = "idx_rc_fecha",    columnList = "fecha_consulta")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroClinico {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
 
    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;
 
    @Column(name = "medico_id", nullable = false)
    private Integer medicoId;
 
    @Column(name = "nombre_medico", nullable = false)
    private String nombreMedico;
 
    @Column(name = "especialidad", nullable = false)
    private String especialidad;
 
    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;
 
    @Column(name = "diagnostico_principal", nullable = false, length = 500)
    private String diagnosticoPrincipal;
 
    @Column(name = "receta_medica", length = 1000)
    private String recetaMedica;
 
    @Column(name = "observaciones", length = 2000)
    private String observaciones;
 
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
