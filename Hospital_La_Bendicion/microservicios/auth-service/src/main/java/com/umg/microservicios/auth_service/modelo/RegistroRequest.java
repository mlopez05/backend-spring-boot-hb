package com.umg.microservicios.auth_service.modelo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroRequest {
    @Email(message = "No corresponde a una estructura de Email")
    @NotNull(message = "El nombre de usuario es obligatorio")
    String usuario;
    @NotBlank(message = "Ingrese el nombre")
    String nombre;
    @NotBlank(message = "Ingrese el apellido")
    String apellido;
    @NotNull(message = "Debe definir una contraseña para el usuario")
    String contraseña;
    Long rol;

    @Builder.Default
    boolean requiereCambioContrasena = false;
}

