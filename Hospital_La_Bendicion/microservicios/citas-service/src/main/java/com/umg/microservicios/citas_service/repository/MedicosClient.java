package com.umg.microservicios.citas_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "medicos-service")
public interface MedicosClient {

    @PostMapping("/api/medico/slots/{slotId}/reservar")
    ResponseEntity<?> reservarSlot(@PathVariable("slotId") Integer slotId);
 
    @PostMapping("/api/medico/slots/{slotId}/liberar")
    ResponseEntity<?> liberarSlot(@PathVariable("slotId") Integer slotId);
}