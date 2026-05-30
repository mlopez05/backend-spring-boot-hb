package com.umg.microservicios.historial_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
 
import java.time.LocalDateTime;
import java.util.List;

public class HistorialDTO {

 
    @Data
    public static class CrearRegistroRequest {
 
        @NotNull(message = "El ID del paciente es obligatorio")
        @JsonProperty("paciente_id")
        private Long pacienteId;
 
        @NotNull(message = "El ID del médico es obligatorio")
        @JsonProperty("medico_id")
        private Integer medicoId;
 
        @NotBlank(message = "El nombre del médico es obligatorio")
        @JsonProperty("nombre_medico")
        private String nombreMedico;
 
        @NotBlank(message = "La especialidad es obligatoria")
        private String especialidad;
 
        @NotNull(message = "La fecha de consulta es obligatoria")
        @JsonProperty("fecha_consulta")
        private LocalDateTime fechaConsulta;
 
        @NotBlank(message = "El diagnóstico principal es obligatorio")
        @JsonProperty("diagnostico_principal")
        private String diagnosticoPrincipal;
 
        @JsonProperty("receta_medica")
        private String recetaMedica;
 
        private String observaciones;
    }
 
    @Data
    public static class RegistroItemDTO {
        private Long idRegistro;
        private LocalDateTime fechaConsulta;
        private MedicoDTO medico;
        private String diagnosticoPrincipal;
        private String recetaMedica;
        private String observaciones;
    }
 
    @Data
    public static class MedicoDTO {
        private Integer idMedico;
        private String nombre;
        private String especialidad;
    }
 
 
    @Data
    public static class HistorialPaginadoResponse {
        private String status;
        private DatosHistorial data;
 
        @Data
        public static class DatosHistorial {
            private Long pacienteId;
            private PaginacionDTO paginacion;
            private List<RegistroItemDTO> registros;
        }
 
        @Data
        public static class PaginacionDTO {
            private long totalRegistros;
            private int paginaActual;
            private int totalPaginas;
        }
    }
}
