package com.example.solfamidasback.controller;

import com.example.solfamidasback.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
public class RoleController {
    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createRol(@PathVariable Integer idFormation, @PathVariable Integer idUser ) {

        return ResponseEntity.ok("ok");
    }


}
