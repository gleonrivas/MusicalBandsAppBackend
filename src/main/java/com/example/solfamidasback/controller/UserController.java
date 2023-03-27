package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.User;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("user")
public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    @GetMapping("/list")
    public @ResponseBody List<User> listUsers() throws JsonProcessingException {

        return Collections.singletonList(userRepository.findAllByActiveIsTrue());

    }

    @GetMapping("/list/{name}")
    public @ResponseBody List<User> listUsersByName(@PathVariable String name) throws JsonProcessingException {

        return Collections.singletonList(userRepository.findAllByNameAndActiveIsTrue(name));

    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user){


        boolean exist = userRepository.findUserByEmail(user.getEmail());
        if(exist){
            return "this email is not available";
        }else {
            userRepository.save(user);
            return "user created successfully";
        }

    }




}
