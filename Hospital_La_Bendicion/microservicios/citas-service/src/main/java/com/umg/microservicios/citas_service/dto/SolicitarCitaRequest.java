package com.umg.microservicios.citas_service.dto;

import java.time.LocalDateTime;


public record SolicitarCitaRequest(
        Integer slotId,
        Integer medicoId,
        LocalDateTime fechaHora,
        String especialidad,
        String medicoAsignado,
        String observaciones
) {}
