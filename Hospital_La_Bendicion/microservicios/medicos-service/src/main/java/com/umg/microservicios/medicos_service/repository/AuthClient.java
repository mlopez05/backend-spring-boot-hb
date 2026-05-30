package com.umg.microservicios.medicos_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.umg.microservicios.medicos_service.dto.AuthResponse;
import com.umg.microservicios.medicos_service.dto.RegistroRequest;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthClient {
        
    @PostMapping("/auth/registro")
    AuthResponse registro(@RequestBody RegistroRequest request);
}
