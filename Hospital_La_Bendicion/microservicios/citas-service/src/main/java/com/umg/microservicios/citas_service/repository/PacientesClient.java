package com.umg.microservicios.citas_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "pacientes-service")
public interface PacientesClient {

    @GetMapping("/api/pacientes/email")
    Map<String, Object> buscarPorEmail(@RequestParam("email") String email);
}