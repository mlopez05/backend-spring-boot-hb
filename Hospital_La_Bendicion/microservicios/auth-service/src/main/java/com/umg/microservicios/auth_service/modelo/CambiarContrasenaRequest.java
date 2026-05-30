package com.umg.microservicios.auth_service.modelo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CambiarContrasenaRequest {

    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;
 
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String contrasenaActual;
 
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
    private String nuevaContrasena;
}
