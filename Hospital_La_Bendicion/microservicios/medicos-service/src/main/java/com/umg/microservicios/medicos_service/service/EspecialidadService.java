package com.umg.microservicios.medicos_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umg.microservicios.medicos_service.model.Especialidad;
import com.umg.microservicios.medicos_service.repository.EspecialidadRepository;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    public Especialidad guardar(Especialidad especialidad){
        return especialidadRepository.save(especialidad);
    }

    public List<Especialidad> listar(){
        return especialidadRepository.findAll();
    }

    public Especialidad buscar(Integer id){
        return especialidadRepository.findById(id).orElse(null);
    }
}
