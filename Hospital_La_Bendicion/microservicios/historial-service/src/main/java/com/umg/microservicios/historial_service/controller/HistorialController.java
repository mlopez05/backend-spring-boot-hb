package com.umg.microservicios.historial_service.controller;

import com.umg.microservicios.historial_service.dto.HistorialDTO.*;
import com.umg.microservicios.historial_service.model.RegistroClinico;
import com.umg.microservicios.historial_service.service.HistorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.Map;
 
@RestController
@RequestMapping("/api/historial-medico")
@RequiredArgsConstructor
public class HistorialController {

    private final HistorialService historialService;

    @GetMapping("/pacientes/{pacienteId}/registros")
    public ResponseEntity<HistorialPaginadoResponse> consultarHistorial(
            @PathVariable Long pacienteId,
            @RequestParam(defaultValue = "1")   int page,
            @RequestParam(defaultValue = "10")  int limit,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestHeader("Authorization") String authHeader) {
 
        HistorialPaginadoResponse response =
                historialService.consultarHistorial(pacienteId, page, limit, sort, authHeader);
 
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registros")
    public ResponseEntity<?> crearRegistro(
            @Valid @RequestBody CrearRegistroRequest request,
            @RequestHeader("Authorization") String authHeader) {
 
        RegistroClinico creado = historialService.crearRegistro(request, authHeader);
 
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status",  "success",
                "message", "Registro clínico creado exitosamente.",
                "data", Map.of(
                        "id_registro",    creado.getId(),
                        "paciente_id",    creado.getPacienteId(),
                        "fecha_consulta", creado.getFechaConsulta()
                )
        ));
    }
}
