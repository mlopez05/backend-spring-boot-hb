package com.umg.microservicios.medicos_service.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Integer id;
}
