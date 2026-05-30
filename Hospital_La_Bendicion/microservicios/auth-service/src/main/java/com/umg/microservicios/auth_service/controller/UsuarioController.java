package com.umg.microservicios.auth_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umg.microservicios.auth_service.modelo.UsuarioResponse;
import com.umg.microservicios.auth_service.servicio.UsuarioServicio;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServicio usuarioServicio;

    @GetMapping("/lista")
    public List<UsuarioResponse> listar(){
        return usuarioServicio.listaUsuarios();
    }
}
