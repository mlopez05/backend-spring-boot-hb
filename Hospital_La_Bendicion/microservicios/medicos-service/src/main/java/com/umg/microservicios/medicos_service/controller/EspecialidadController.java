package com.umg.microservicios.medicos_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umg.microservicios.medicos_service.model.Especialidad;
import com.umg.microservicios.medicos_service.service.EspecialidadService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/especialidad")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @PostMapping
    public ResponseEntity<Especialidad> guardar(@RequestBody Especialidad especialidad){
        return ResponseEntity.ok(especialidadService.guardar(especialidad));
    }

    @GetMapping
    public ResponseEntity<List<Especialidad>> listar(){
        return ResponseEntity.ok(especialidadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> buscar(@PathVariable Integer id){
        return ResponseEntity.ok(especialidadService.buscar(id));
    }
}
