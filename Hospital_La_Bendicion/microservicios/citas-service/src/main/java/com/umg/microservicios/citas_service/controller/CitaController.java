package com.umg.microservicios.citas_service.controller;
 
import com.umg.microservicios.citas_service.dto.CancelarCitaRequest;
import com.umg.microservicios.citas_service.dto.CitasResponseDTO;
import com.umg.microservicios.citas_service.dto.ReprogramarCitaRequest;
import com.umg.microservicios.citas_service.dto.SolicitarCitaRequest;
import com.umg.microservicios.citas_service.model.Cita;
import com.umg.microservicios.citas_service.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {
 
    private final CitaService citaService;
 
    @GetMapping
    public ResponseEntity<CitasResponseDTO> getMisCitas(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(citaService.obtenerHistorialPaciente(authHeader));
    }
 
    @PostMapping
    public ResponseEntity<?> solicitarCita(
            @RequestBody SolicitarCitaRequest request,
            @RequestHeader("Authorization") String authHeader) {
 
        Cita nueva = citaService.solicitarCita(request, authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status",  "success",
                "message", "Cita agendada exitosamente.",
                "data", Map.of(
                        "id_cita",    nueva.getId(),
                        "estado",     nueva.getEstado(),
                        "fecha_hora", nueva.getFechaHora(),
                        "especialidad", nueva.getEspecialidad()
                )
        ));
    }

    @PatchMapping("/{id}/reprogramar")
    public ResponseEntity<?> reprogramar(
            @PathVariable Long id,
            @RequestBody ReprogramarCitaRequest request,
            @RequestHeader("Authorization") String authHeader) {
 
        Cita actualizada = citaService.reprogramarCita(id, request, authHeader);
        return ResponseEntity.ok(Map.of(
                "status",  "success",
                "message", "Cita reprogramada correctamente.",
                "data", Map.of(
                        "id_cita",        actualizada.getId(),
                        "nueva_fecha",    actualizada.getFechaHora(),
                        "estado",         actualizada.getEstado()
                )
        ));
    }
 
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(
            @PathVariable Long id,
            @RequestBody(required = false) CancelarCitaRequest request,
            @RequestHeader("Authorization") String authHeader) {
 
        Cita cancelada = citaService.cancelarCita(id, request, authHeader);
        return ResponseEntity.ok(Map.of(
                "status",  "success",
                "message", "Cita médica cancelada exitosamente.",
                "data", Map.of(
                        "id_cita",           cancelada.getId(),
                        "estado",            cancelada.getEstado(),
                        "fecha_cancelacion", LocalDateTime.now().toString()
                )
        ));
    }
}