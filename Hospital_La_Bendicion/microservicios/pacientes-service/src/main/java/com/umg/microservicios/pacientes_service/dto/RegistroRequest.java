package com.umg.microservicios.pacientes_service.dto;

import lombok.Data;

@Data
public class RegistroRequest {
    private String usuario;
    private String nombre;
    private String apellido;
    private String contraseña;
    private Integer rol;
}
