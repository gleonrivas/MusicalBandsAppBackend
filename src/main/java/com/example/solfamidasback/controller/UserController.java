package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.IUserConverter;
import com.example.solfamidasback.model.DTO.UserDTO;
import com.example.solfamidasback.model.User;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    IUserConverter userConverter;


    @GetMapping("/list")
    public @ResponseBody List<User> listUsers() throws JsonProcessingException {

        return userRepository.findAllByActiveIsTrue();

    }

    @GetMapping("/list/{name}")
    public @ResponseBody List<User> listUsersByName(@PathVariable String name) throws JsonProcessingException {

        return userRepository.findAllByNameAndActiveIsTrue(name);

    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO){


        if(userRepository.findUserByEmailAndActiveIsTrue(userDTO.getEmail())!=null){
            return  "this email is not available";
        }else {
            userRepository.save(userConverter.toEntity(userDTO));
            return "user created successfully";
        }

    }




}