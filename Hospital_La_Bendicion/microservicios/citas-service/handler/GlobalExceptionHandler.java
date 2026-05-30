package com.umg.microservicios.citas_service.handler;

import com.umg.microservicios.citas_service.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja el Error 404 (Cita no encontrada)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "NOT_FOUND: La cita solicitada no existe."));
    }

    // Maneja el Error 400 (Regla de 24 horas)
    @ExceptionHandler(PolicyException.class)
    public ResponseEntity<?> handlePolicy(PolicyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Maneja el Error 409 (Slot ocupado - Concurrencia)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "CONFLICT: El horario seleccionado ya no está disponible."));
    }

    // Maneja el Error 503 (Falla de ms-medicos o Timeout)
    @ExceptionHandler(Exception.class) // Puedes ser más específico con FeignException
    public ResponseEntity<?> handleGlobal(Exception ex) {
        if (ex.getMessage().contains("ms-medicos")) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "SERVICE_UNAVAILABLE: No se pudo contactar con la agenda médica. Intente más tarde."));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor."));
    }
}