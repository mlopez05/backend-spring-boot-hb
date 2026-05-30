package com.umg.microservicios.pacientes_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.umg.microservicios.pacientes_service.model.SeguroMedico;
import com.umg.microservicios.pacientes_service.service.SeguroMedicoService;

import java.util.List;

@RestController
@RequestMapping("/api/seguros")
@CrossOrigin(origins = "*")
public class SeguroMedicoController {
    @Autowired
    private SeguroMedicoService seguroMedicoService;

    // GET /api/seguros
    @GetMapping
    public ResponseEntity<List<SeguroMedico>> listar() {
        return ResponseEntity.ok(seguroMedicoService.listar());
    }

    // GET /api/seguros/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SeguroMedico> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(seguroMedicoService.obtenerPorId(id));
    }

    // POST /api/seguros
    @PostMapping
    public ResponseEntity<SeguroMedico> crear(@RequestBody SeguroMedico seguro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seguroMedicoService.crear(seguro));
    }

    // PUT /api/seguros/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SeguroMedico> actualizar(
            @PathVariable Integer id, @RequestBody SeguroMedico datos) {
        return ResponseEntity.ok(seguroMedicoService.actualizar(id, datos));
    }

    // DELETE /api/seguros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        seguroMedicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
