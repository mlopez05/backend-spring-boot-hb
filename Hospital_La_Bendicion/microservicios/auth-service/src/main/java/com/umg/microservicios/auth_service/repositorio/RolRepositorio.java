package com.umg.microservicios.auth_service.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umg.microservicios.auth_service.modelo.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Long> {
    
}
