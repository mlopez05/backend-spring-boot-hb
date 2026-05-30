package com.umg.microservicios.medicos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoResponse {

    private Integer id;
    private String numero_colegiado;
    private String estado;
    private boolean cuenta_usuario_creada;
}
