package com.umg.microservicios.medicos_service.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umg.microservicios.medicos_service.dto.AuthResponse;
import com.umg.microservicios.medicos_service.dto.MedicoRequest;
import com.umg.microservicios.medicos_service.dto.MedicoResponse;
import com.umg.microservicios.medicos_service.dto.RegistroRequest;
import com.umg.microservicios.medicos_service.model.Especialidad;
import com.umg.microservicios.medicos_service.model.Hospital;
import com.umg.microservicios.medicos_service.model.Medico;
import com.umg.microservicios.medicos_service.repository.AuthClient;
import com.umg.microservicios.medicos_service.repository.MedicoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    public MedicoResponse guardar(MedicoRequest medicoRequest) {

        RegistroRequest registro = new RegistroRequest();
        registro.setUsuario(medicoRequest.getEmail());
        registro.setNombre(medicoRequest.getNombres());
        registro.setApellido(medicoRequest.getApellidos());
        registro.setContraseña("1234");
        registro.setRol(2);
        
        registro.setRequiereCambioContrasena(true);

        AuthResponse response = authClient.registro(registro);

        if (response == null) {
            throw new RuntimeException("Error al tratar de registrar usuario.");
        }

        Especialidad especialidad = especialidadService.buscar(medicoRequest.getEspecialidad());
        Hospital hospital = hospitalService.buscar(medicoRequest.getHospital());

        if (especialidad == null) {
            throw new RuntimeException("La especialidad con ID " + medicoRequest.getEspecialidad() + " no existe.");
        }

        if (hospital == null) {
            throw new RuntimeException("El hospital con ID " + medicoRequest.getHospital() + " no existe.");
        }

        Medico medico = Medico.builder()
                .nombres(medicoRequest.getNombres())
                .apellidos(medicoRequest.getApellidos())
                .noColegiado(medicoRequest.getNumeroColegiado())
                .especialidad(especialidad)
                .email(medicoRequest.getEmail())
                .telefono(medicoRequest.getTelefono())
                .hospital(hospital)
                .idUsuario(response.getId())
                .build();

        Medico medicoCreado = medicoRepository.save(medico);

        // Publicar evento → notificacion-service envía correo al médico
        try {
            java.util.Map<String, Object> datosEvento = new java.util.HashMap<>();
            datosEvento.put("idUsuario",  medicoCreado.getIdUsuario());
            datosEvento.put("nombres",    medicoCreado.getNombres());
            datosEvento.put("apellidos",  medicoCreado.getApellidos());
            datosEvento.put("email",      medicoCreado.getEmail());

            rabbitTemplate.convertAndSend("ecosistema.exchange.topicos", "medicos.creado", datosEvento);
        } catch (Exception e) {
            System.err.println("No se pudo publicar el evento en RabbitMQ: " + e.getMessage());
        }

        MedicoResponse medicoResponse = new MedicoResponse();
        medicoResponse.setId(medicoCreado.getId());
        medicoResponse.setNumero_colegiado(medicoCreado.getNoColegiado());
        medicoResponse.setEstado("ACTIVO");
        medicoResponse.setCuenta_usuario_creada(true);

        return medicoResponse;
    }

    public Medico buscar(Integer id) {
        return medicoRepository.findById(id).orElse(null);
    }
}