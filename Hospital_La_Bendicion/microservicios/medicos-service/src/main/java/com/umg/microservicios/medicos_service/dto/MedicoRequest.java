package com.umg.microservicios.medicos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoRequest {

    private String nombres;
    private String apellidos;
    private String numeroColegiado;
    private Integer especialidad;
    private String email;
    private String telefono;
    private Integer hospital;
}
