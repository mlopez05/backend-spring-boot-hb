package com.umg.microservicios.auth_service.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String rol;
    private boolean requiereCambioContrasena;
}
