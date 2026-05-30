package com.umg.microservicios.auth_service.servicio;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.umg.microservicios.auth_service.exception.UsuarioNotFoundException;
import com.umg.microservicios.auth_service.modelo.AuthResponse;
import com.umg.microservicios.auth_service.modelo.CambiarContrasenaRequest;
import com.umg.microservicios.auth_service.modelo.LoginRequest;
import com.umg.microservicios.auth_service.modelo.RegistroRequest;
import com.umg.microservicios.auth_service.modelo.Rol;
import com.umg.microservicios.auth_service.modelo.Usuario;
import com.umg.microservicios.auth_service.repositorio.RolRepositorio;
import com.umg.microservicios.auth_service.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContraseña()));
        Usuario usuario = usuarioRepositorio.findByUsuario(request.getUsuario()).orElseThrow(
            () -> new UsuarioNotFoundException(String.format("Usuario %s no encontrado", request.getUsuario()))
        );
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder()
                .token(token)
                .id(usuario.getId())
                .requiereCambioContrasena(
                    usuario.isRequiereCambioContrasena() ? true : null
                )
                .build();
    }

    public AuthResponse registro(RegistroRequest request) {
        Rol rol = rolRepositorio.findById(request.getRol()).orElseThrow(() -> new RuntimeException("Rol no encontrado."));

        Usuario usuario = Usuario.builder()
        .usuario(request.getUsuario())
        .nombre(request.getNombre())
        .apellido(request.getApellido())
        .contraseña(passwordEncoder.encode(request.getContraseña()))
        .rol(rol)
        .requiereCambioContrasena(false)
        .build();

        usuarioRepositorio.save(usuario);

        return AuthResponse.builder()
            .token(jwtService.getToken(usuario))
            .id(usuario.getId())
            .build();
    }

    public AuthResponse cambiarContrasena(CambiarContrasenaRequest request) {
        Usuario usuario = usuarioRepositorio.findByUsuario(request.getUsuario())
            .orElseThrow(() -> new UsuarioNotFoundException(
                String.format("Usuario %s no encontrado", request.getUsuario())));
 
        // Validar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.getContrasenaActual(), usuario.getContraseña())) {
            throw new RuntimeException("La contraseña actual es incorrecta.");
        }
 
        // Validar que la nueva contraseña sea diferente a la temporal
        if (passwordEncoder.matches(request.getNuevaContrasena(), usuario.getContraseña())) {
            throw new RuntimeException("La nueva contraseña no puede ser igual a la contraseña actual.");
        }
 
        usuario.setContraseña(passwordEncoder.encode(request.getNuevaContrasena()));
        usuario.setRequiereCambioContrasena(false);
        usuarioRepositorio.save(usuario);
 
        // Emitir nuevo token con el flag ya en false
        return AuthResponse.builder()
                .token(jwtService.getToken(usuario))
                .id(usuario.getId())
                .build();
    }
}
