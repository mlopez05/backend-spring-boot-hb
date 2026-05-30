package com.umg.microservicios.notificacion_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
import java.util.Map;

@FeignClient(name = "pacientes-service")
public interface PacientesClient {

    @GetMapping("/api/pacientes/{id}")
    Map<String, Object> obtenerPaciente(@PathVariable("id") Integer id);
}
