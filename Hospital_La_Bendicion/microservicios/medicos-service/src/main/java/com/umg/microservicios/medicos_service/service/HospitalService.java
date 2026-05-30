package com.umg.microservicios.medicos_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umg.microservicios.medicos_service.model.Hospital;
import com.umg.microservicios.medicos_service.repository.HospitalRepository;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public Hospital guardar(Hospital hospital){
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> listar(){
        return hospitalRepository.findAll();
    }

    public Hospital buscar(Integer id){
        return hospitalRepository.findById(id).orElse(null);
    }
}
