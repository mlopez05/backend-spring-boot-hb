package com.umg.microservicios.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acceso")
public class SaludoController {

    @GetMapping("/sistema")
    public String saludoBienvenida(){
        return "Bienvenido al sistema de Hospital la Bendición";
    }
}
