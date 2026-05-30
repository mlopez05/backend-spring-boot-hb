package com.umg.microservicios.auth_service.exception;

import java.util.HashMap;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.umg.microservicios.common_exceptions.ErrorResponse;
import com.umg.microservicios.common_exceptions.GlobalExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice(basePackages = "com.umg.microservicios.auth_service")
@Primary
@Slf4j
public class UsuarioExceptionHandler extends GlobalExceptionHandler{

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UsuarioNotFoundException exception){
        var errors = new HashMap<String, String>();
        var fieldName = "Usuario";
        errors.put(fieldName, exception.getMessage());

        log.warn("Usuario no encontrado: {}", exception.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
    }

}