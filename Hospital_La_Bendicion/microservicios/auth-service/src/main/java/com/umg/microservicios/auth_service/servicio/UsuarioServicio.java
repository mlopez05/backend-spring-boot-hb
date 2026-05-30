package com.umg.microservicios.auth_service.servicio;

import java.util.List;

import org.springframework.stereotype.Service;

import com.umg.microservicios.auth_service.modelo.UsuarioResponse;
import com.umg.microservicios.auth_service.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServicio{

    private final UsuarioRepositorio usuarioRepositorio;

    public List<UsuarioResponse> listaUsuarios() {
        return usuarioRepositorio.findAll().stream()
                .map(u -> new UsuarioResponse(
                        u.getId(),
                        u.getUsuario(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getRol().getNombre(),
                        u.isRequiereCambioContrasena()
                ))
                .toList();
    }
}
