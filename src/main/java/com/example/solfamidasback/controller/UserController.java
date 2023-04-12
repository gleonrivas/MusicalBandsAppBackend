package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "User", description = "User crud")
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Operation(summary = "Retrieve a list of active user ",
            description = "The response is a list of active User Objects")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Users.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/list")
    public ResponseEntity<List<Users>>  listUsers() {
        List<Users> listUsers = userRepository.findAllByActiveIsTrue();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(listUsers,headers, HttpStatus.OK);
    }
    @Operation(summary = "Retrieve a list of all user ",
            description = "The response is a list of all User Objects")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Users.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/listAll")
    public ResponseEntity<String> todosLosUser(){
        return ResponseEntity.ok("Se verian todos los usuarios");
    }
    @Operation(summary = "Retrieve list of user with similar name and are actives",
            description = "The response is a list of User with similar name",
            tags = "name")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Users.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
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
