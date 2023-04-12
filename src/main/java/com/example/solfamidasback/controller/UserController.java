package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.UserUpdateDTO;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;




    @GetMapping("/list")
    public ResponseEntity<List<Users>>  listUsers() {
        List<Users> listUsers = userRepository.findAllByActiveIsTrue();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(listUsers,headers, HttpStatus.OK);
    }


//    @PutMapping("/update")
//    public ResponseEntity<Users> updateUser(@RequestBody UserUpdateDTO userUpdateDTO){
//
//        Users user = userRepository.findByIdAndActiveIsTrue(userUpdateDTO.getId());
//
//        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        if (userUpdateDTO.getId() == null){
//            String mensaje = "Id de usuario no válido";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.TEXT_PLAIN);
//            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
//        }
//
//
//        user.setName(userUpdateDTO.getName());
//        user.setSurName(userUpdateDTO.getSurName());
//        user.setEmail(userUpdateDTO.getEmail());
//        user.setBirthDate(LocalDateTime.parse(userUpdateDTO.getBirthDate().replace(" ", "T"), formater));
//        user.setDni(userUpdateDTO.getDni());
//        user.setSuperadmin(userUpdateDTO.getSuperadmin());
//
//        if (userUpdateDTO.getPassword() != null) {
//            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
//        }
//
//        userRepository.save(user);
//
//
//        String mensaje = "Usuario cambiado con éxito";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        return new ResponseEntity(mensaje, headers, HttpStatus.OK);
//
//    }



    @GetMapping("/listAll")
    public ResponseEntity<String> todosLosUser(){
        return ResponseEntity.ok("Se verian todos los usuarios");
    }

    @GetMapping("/list/{name}")
    public @ResponseBody List<Users> listUsersByName(@PathVariable String name) throws JsonProcessingException {

        return Collections.singletonList(userRepository.findAllByNameAndActiveIsTrue(name));

    }

    @PostMapping("/register")
    public String registerUser(@RequestBody Users user){


        boolean exist = userRepository.existsByEmail(user.getEmail());
        if(exist){
            return "this email is not available";
        }else {
            userRepository.save(user);
            return "user created successfully";
        }

    }




}
