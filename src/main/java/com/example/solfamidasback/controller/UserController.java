package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.UserConverter;
import com.example.solfamidasback.model.DTO.UserDTO;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.JwtService;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {


    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserConverter userConverter;

    @Autowired
    UserService userService;


    @GetMapping("/list")
    public ResponseEntity<List<Users>>  listUsers() {
        List<Users> listUsers = userRepository.findAllByActiveTrue();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(listUsers,headers, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<String> todosLosUser(){
        return ResponseEntity.ok("Se verian todos los usuarios");
    }

    @GetMapping("/list/{name}")
    public @ResponseBody List<Users> listUsersByName(@PathVariable String name) throws JsonProcessingException {

        return Collections.singletonList(userRepository.findAllByNameAndActiveIsTrue(name));

    }

    @PostMapping("/editProfile")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        userRepository.save(userConverter.toEntity(user));
        return new ResponseEntity("user created successfully",headers, HttpStatus.OK);

    }

    @GetMapping("/profile")
    public ResponseEntity<Users> profile(HttpServletRequest request){
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO userDTO = userConverter.toDTO(user);
        return new ResponseEntity(userDTO,headers, HttpStatus.OK);

    }

    @DeleteMapping("/deleteProfile")
    public ResponseEntity<String> DeleteProfile(HttpServletRequest request){
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        user.setActive(false);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("user deleted successfully",headers, HttpStatus.OK);

    }






}
