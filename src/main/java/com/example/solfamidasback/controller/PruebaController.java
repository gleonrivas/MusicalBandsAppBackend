package com.example.solfamidasback.controller;

import org.junit.runner.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/prueba-controller")
public class PruebaController {
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Controlador de prueba funcionando");
    }


}
