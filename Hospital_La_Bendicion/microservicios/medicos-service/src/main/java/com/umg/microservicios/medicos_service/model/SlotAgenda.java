package com.umg.microservicios.medicos_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "slot_agenda")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotAgenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_slot")
    private Integer id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
 
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
 
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
 
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "estado", nullable = false)
    @Builder.Default
    private String estado = "DISPONIBLE";
}
