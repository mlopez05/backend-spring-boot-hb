package com.umg.microservicios.notificacion_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umg.microservicios.notificacion_service.model.Notificacion;
import com.umg.microservicios.notificacion_service.service.NotificacionService;

@RestController
@RequestMapping("/api/notificacion")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(@PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.listarPorUsuario(idUsuario);

        if (notificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notificaciones);
    }

    @PatchMapping("/{id}/leido")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Integer id) {
        try {
            Notificacion actualizada = notificacionService.marcarComoLeida(id);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build(); 
        }
    }    

 }
