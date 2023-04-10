package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.FormationDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
