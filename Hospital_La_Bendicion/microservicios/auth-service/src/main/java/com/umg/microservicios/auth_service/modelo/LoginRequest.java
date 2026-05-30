package com.umg.microservicios.auth_service.modelo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor  
public class LoginRequest {
    @NotNull(message = "Por favor ingrese el nombre de usuario")
    String usuario;
    @NotNull(message = "Debe ingresar la contraseña del usuario")
    String contraseña;
}

