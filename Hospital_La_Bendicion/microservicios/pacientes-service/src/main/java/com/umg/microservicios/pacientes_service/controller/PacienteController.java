package com.umg.microservicios.pacientes_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.umg.microservicios.pacientes_service.dto.AuthResponse;
import com.umg.microservicios.pacientes_service.model.Paciente;
import com.umg.microservicios.pacientes_service.model.PacienteRequest;
import com.umg.microservicios.pacientes_service.service.PacienteService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    // POST /api/pacientes?idRecepcionista=1&idSeguro=2
    // Registrar nuevo paciente 
    @PostMapping
    public ResponseEntity<AuthResponse> registrarPaciente(
        @Valid @RequestBody PacienteRequest request) {

        AuthResponse nuevo = pacienteService.registrarPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // GET /api/pacientes
    // Listar todos los pacientes
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    // GET /api/pacientes/{id}
    // Obtener paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPaciente(@PathVariable Integer id) {
        return ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }

    // GET /api/pacientes/identificacion/{numero}
    // Buscar paciente por DPI/CUI
    @GetMapping("/identificacion/{numero}")
    public ResponseEntity<Paciente> obtenerPorIdentificacion(@PathVariable String numero) {
        return ResponseEntity.ok(pacienteService.obtenerPorIdentificacion(numero));
    }

    // GET /api/pacientes/buscar?nombre
    // Buscar pacientes por nombre o apellido
    @GetMapping("/buscar")
    public ResponseEntity<List<Paciente>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(pacienteService.buscarPorNombre(nombre));
    }
    
    @GetMapping("/email")
    public ResponseEntity<Map<String, Object>> buscarPorEmail(@RequestParam String email) {
    Paciente p = pacienteService.buscarPorEmail(email);
    Map<String, Object> response = new HashMap<>();
    response.put("idPaciente", p.getIdPaciente());
    response.put("nombre", p.getNombre());
    response.put("apellido", p.getApellido());
    response.put("email", p.getEmail());
    return ResponseEntity.ok(response);
}

    // PUT /api/pacientes/{id}
    // Actualizar datos del paciente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(
            @PathVariable Integer id,
            @Valid @RequestBody Paciente paciente) {

        return ResponseEntity.ok(pacienteService.actualizarPaciente(id, paciente));
    }

    // DELETE /api/pacientes/{id}
    // Eliminar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Integer id) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }

}
