package com.umg.microservicios.citas_service.dto;

import com.umg.microservicios.citas_service.model.Cita;
import java.util.List;

public record CitasResponseDTO(
    List<Cita> citasFuturas,
    List<Cita> citasPasadas
) {}