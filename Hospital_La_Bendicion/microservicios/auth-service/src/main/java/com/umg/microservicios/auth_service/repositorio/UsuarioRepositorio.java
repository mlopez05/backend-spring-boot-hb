package com.umg.microservicios.auth_service.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umg.microservicios.auth_service.modelo.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByUsuario(String usuario);
    
}
