package com.umg.microservicios.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umg.microservicios.auth_service.modelo.AuthResponse;
import com.umg.microservicios.auth_service.modelo.CambiarContrasenaRequest;
import com.umg.microservicios.auth_service.modelo.LoginRequest;
import com.umg.microservicios.auth_service.modelo.RegistroRequest;
import com.umg.microservicios.auth_service.servicio.AuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@Valid @RequestBody RegistroRequest request){
        return ResponseEntity.ok(authService.registro(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PatchMapping("/cambiar-contrasena")
    public ResponseEntity<AuthResponse> cambiarContrasena(
            @Valid @RequestBody CambiarContrasenaRequest request) {
        return ResponseEntity.ok(authService.cambiarContrasena(request));
    }
}
