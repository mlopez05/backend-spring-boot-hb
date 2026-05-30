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

import com.umg.microservicios.medicos_service.model.Hospital;
import com.umg.microservicios.medicos_service.service.HospitalService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<Hospital> guardar(@RequestBody Hospital hospital){
        return ResponseEntity.ok(hospitalService.guardar(hospital));
    }

    @GetMapping
    public ResponseEntity<List<Hospital>> listar(){
        return ResponseEntity.ok(hospitalService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hospital> buscar(@PathVariable Integer id){
        return ResponseEntity.ok(hospitalService.buscar(id));
    }
}
