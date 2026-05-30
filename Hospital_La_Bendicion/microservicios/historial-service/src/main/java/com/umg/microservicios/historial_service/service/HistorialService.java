package com.umg.microservicios.historial_service.service;

import com.umg.microservicios.historial_service.config.JwtUtil;
import com.umg.microservicios.historial_service.dto.HistorialDTO.*;
import com.umg.microservicios.historial_service.model.RegistroClinico;
import com.umg.microservicios.historial_service.repository.RegistroClinicoRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
 
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistorialService {
    private final RegistroClinicoRepository repository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public HistorialPaginadoResponse consultarHistorial(
            Long pacienteId, int page, int limit, String sort, String authHeader) {
 
        Claims claims = jwtUtil.getClaims(authHeader);
        String rol    = claims.get("rol", String.class);
        String sub    = claims.getSubject();

        boolean esPaciente = "paciente".equalsIgnoreCase(rol);
        boolean esMedico   = "medico".equalsIgnoreCase(rol);
        boolean esAdmin    = "admin".equalsIgnoreCase(rol);
 
        if (!esPaciente && !esMedico && !esAdmin) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "El token no contiene un rol válido. Vuelve a iniciar sesión.");
        }
 
        if (esPaciente) {
            log.info("Paciente {} consultando historial del paciente_id {}", sub, pacienteId);
       
        }
                
        // Paginación — valores por defecto si vienen fuera de rango
        int safePage  = Math.max(page, 1);
        int safeLimit = (limit > 0 && limit <= 100) ? limit : 20;
        Sort.Direction direction = "asc".equalsIgnoreCase(sort)
                ? Sort.Direction.ASC : Sort.Direction.DESC;
 
        Pageable pageable = PageRequest.of(
                safePage - 1,  
                safeLimit,
                Sort.by(direction, "fechaConsulta")
        );
 
        Page<RegistroClinico> paginaResultado = repository.findByPacienteId(pacienteId, pageable);
 
        // Mapear entidades a DTOs (oculta campos internos de BD)
        List<RegistroItemDTO> items = paginaResultado.getContent().stream()
                .map(this::toItemDTO)
                .toList();
 
        // Construir respuesta
        HistorialPaginadoResponse.DatosHistorial datos =
                new HistorialPaginadoResponse.DatosHistorial();
        datos.setPacienteId(pacienteId);
 
        HistorialPaginadoResponse.PaginacionDTO paginacion =
                new HistorialPaginadoResponse.PaginacionDTO();
        paginacion.setTotalRegistros(paginaResultado.getTotalElements());
        paginacion.setPaginaActual(safePage);
        paginacion.setTotalPaginas(paginaResultado.getTotalPages());
        datos.setPaginacion(paginacion);
        datos.setRegistros(items);
 
        HistorialPaginadoResponse response = new HistorialPaginadoResponse();
        response.setStatus("success");
        response.setData(datos);
 
        return response;
    }
 
    @Transactional
    public RegistroClinico crearRegistro(CrearRegistroRequest request, String authHeader) {
        Claims claims = jwtUtil.getClaims(authHeader);
        String rol    = claims.get("rol", String.class);
 
        if (!"MEDICO".equalsIgnoreCase(rol) && !"ADMINISTRADOR".equalsIgnoreCase(rol)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Solo los médicos pueden registrar consultas en el historial.");
        }
 
        RegistroClinico registro = RegistroClinico.builder()
                .pacienteId(request.getPacienteId())
                .medicoId(request.getMedicoId())
                .nombreMedico(request.getNombreMedico())
                .especialidad(request.getEspecialidad())
                .fechaConsulta(request.getFechaConsulta())
                .diagnosticoPrincipal(request.getDiagnosticoPrincipal())
                .recetaMedica(request.getRecetaMedica())
                .observaciones(request.getObservaciones())
                .build();
 
        RegistroClinico guardado = repository.save(registro);
        log.info("Registro clínico {} creado para paciente {} por médico {}",
                guardado.getId(), guardado.getPacienteId(), guardado.getMedicoId());
        return guardado;
    }
 
 
    private RegistroItemDTO toItemDTO(RegistroClinico rc) {
        MedicoDTO medicoDTO = new MedicoDTO();
        medicoDTO.setIdMedico(rc.getMedicoId());
        medicoDTO.setNombre(rc.getNombreMedico());
        medicoDTO.setEspecialidad(rc.getEspecialidad());
 
        RegistroItemDTO item = new RegistroItemDTO();
        item.setIdRegistro(rc.getId());
        item.setFechaConsulta(rc.getFechaConsulta());
        item.setMedico(medicoDTO);
        item.setDiagnosticoPrincipal(rc.getDiagnosticoPrincipal());
        item.setRecetaMedica(rc.getRecetaMedica());
        item.setObservaciones(rc.getObservaciones());
        return item;
    }
}
