package com.umg.microservicios.citas_service.dto;

import java.time.LocalDateTime;

public record ReprogramarCitaRequest(
    String nuevoSlotId,
    LocalDateTime nuevaFechaHora
) {}