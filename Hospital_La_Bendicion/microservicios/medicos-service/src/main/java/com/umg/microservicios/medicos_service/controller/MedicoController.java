package com.umg.microservicios.medicos_service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umg.microservicios.medicos_service.dto.MedicoRequest;
import com.umg.microservicios.medicos_service.dto.MedicoResponse;
import com.umg.microservicios.medicos_service.model.Medico;
import com.umg.microservicios.medicos_service.service.MedicoService;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<Medico>> listar(){
        List<Medico> medicos = medicoService.listar();

        if(medicos.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscar(@PathVariable Integer id){

        Medico medicoEncontrado = medicoService.buscar(id);

        if(medicoEncontrado == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(medicoEncontrado);
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> guardar(@RequestBody MedicoRequest medicoRequest){

        MedicoResponse response = medicoService.guardar(medicoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}