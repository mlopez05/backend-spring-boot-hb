package com.umg.microservicios.pacientes_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.umg.microservicios.pacientes_service.dto.AuthResponse;
import com.umg.microservicios.pacientes_service.dto.RegistroRequest;
import com.umg.microservicios.pacientes_service.model.Paciente;
import com.umg.microservicios.pacientes_service.model.PacienteRequest;
import com.umg.microservicios.pacientes_service.model.Recepcionista;
import com.umg.microservicios.pacientes_service.model.SeguroMedico;
import com.umg.microservicios.pacientes_service.repository.AuthClient;
import com.umg.microservicios.pacientes_service.repository.PacienteRepository;
import com.umg.microservicios.pacientes_service.repository.RecepcionistaRepository;
import com.umg.microservicios.pacientes_service.repository.SeguroMedicoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private RecepcionistaRepository recepcionistaRepository;
    @Autowired
    private SeguroMedicoRepository seguroMedicoRepository;
    @Autowired
    private AuthClient authClient;

    public AuthResponse registrarPaciente(PacienteRequest request) {
        RegistroRequest registro = new RegistroRequest();
        registro.setUsuario(request.getEmail());
        registro.setNombre(request.getNombre());
        registro.setApellido(request.getApellido());
        registro.setContraseña(request.getContraseña());
        registro.setRol(request.getRol());
        AuthResponse response = authClient.registro(registro);
        Integer id = response.getId();
        Paciente p = new Paciente();
        if (pacienteRepository.existsByNumeroIdentificacion(request.getNumeroIdentificacion())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un paciente con el número de identificación: " + request.getNumeroIdentificacion());
        }
        Recepcionista recepcionista = recepcionistaRepository.findById(request.getIdRecepcionista())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Recepcionista no encontrado con id: " + request.getIdRecepcionista()));
        p.setRecepcionista(recepcionista);
        if (request.getIdSeguro() != null) {
            SeguroMedico seguro = seguroMedicoRepository.findById(request.getIdSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Seguro médico no encontrado con id: " + request.getIdSeguro()));
            p.setSeguroMedico(seguro);
        }
        p.setNumeroIdentificacion(request.getNumeroIdentificacion());
        p.setNombre(request.getNombre());
        p.setApellido(request.getApellido());
        p.setFechaNacimiento(request.getFechaNacimiento());
        p.setDireccion(request.getDireccion());
        p.setTelefono(request.getTelefono());
        p.setEmail(request.getEmail());
        p.setFechaRegistro(LocalDateTime.now());
        p.setId_usuario(id);
        pacienteRepository.save(p);
        return response;
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente obtenerPorId(Integer id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Paciente no encontrado con id: " + id));
    }

    public Paciente obtenerPorIdentificacion(String numeroIdentificacion) {
        return pacienteRepository.findByNumeroIdentificacion(numeroIdentificacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Paciente no encontrado con identificación: " + numeroIdentificacion));
    }

    public List<Paciente> buscarPorNombre(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(nombre, nombre);
    }

/*     public Paciente buscarPorEmail(String email) {
        return pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Paciente no encontrado con email: " + email));
    } */

    public Paciente buscarPorEmail(String email) {
    return pacienteRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Paciente no encontrado con email: " + email));
}

    public Paciente actualizarPaciente(Integer id, Paciente datosActualizados) {
        Paciente pacienteExistente = obtenerPorId(id);
        pacienteExistente.setNombre(datosActualizados.getNombre());
        pacienteExistente.setApellido(datosActualizados.getApellido());
        pacienteExistente.setFechaNacimiento(datosActualizados.getFechaNacimiento());
        pacienteExistente.setDireccion(datosActualizados.getDireccion());
        pacienteExistente.setTelefono(datosActualizados.getTelefono());
        pacienteExistente.setEmail(datosActualizados.getEmail());
        return pacienteRepository.save(pacienteExistente);
    }

    public void eliminarPaciente(Integer id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Paciente no encontrado con id: " + id);
        }
        pacienteRepository.deleteById(id);
    }
}