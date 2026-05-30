package com.umg.microservicios.auth_service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UsuarioNotFoundException extends RuntimeException {
    private final String message;
}
