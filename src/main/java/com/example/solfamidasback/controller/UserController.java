package com.example.solfamidasback.controller;

import com.example.solfamidasback.configSecurity.driveCredentials.GoogleDriveBasic;
import com.example.solfamidasback.controller.DTO.PasswordDTO;
import com.example.solfamidasback.model.DTO.SuperAdminDTO;
import com.example.solfamidasback.model.DTO.UserConverter;
import com.example.solfamidasback.model.DTO.UserDTO;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.AuthenticationService;
import com.example.solfamidasback.service.JwtService;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
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

    @Autowired
    AuthenticationService authenticateService;
    @Autowired
    private PasswordEncoder passwordEncoder;


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
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user, HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users userSession = userRepository.findByEmailAndActiveTrue(mail);
        userSession.setName(user.getName());
        userSession.setSurName(user.getSurName());
        userSession.setEmail(user.getEmail());
        userSession.setBirthDate(LocalDate.parse(user.getBirthDate()).atStartOfDay());
        userSession.setDni(user.getDni());

        userRepository.save(userSession);
        return new ResponseEntity("user edited successfully",headers, HttpStatus.OK);

    }

    @GetMapping("/profile")
    public ResponseEntity<Users> profile(HttpServletRequest request) throws IOException {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO userDTO = userConverter.toDTO(user);
        GoogleDriveBasic driveBasic = new GoogleDriveBasic();
        driveBasic.getURL();
        driveBasic.uploadTextFile("../src/main/java/com/example/solfamidasback/configSecurity/driveCredentials/a.png", "a");
        return new ResponseEntity(userDTO,headers, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProfile")
    public ResponseEntity<String> deleteProfile(HttpServletRequest request){
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        user.setActive(false);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("user deleted successfully",headers, HttpStatus.OK);

    }


    @PostMapping("/createAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody SuperAdminDTO user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        userRepository.save(userConverter.fromAdmintoEntity(user));
        return new ResponseEntity("admin created successfully",headers, HttpStatus.OK);

    }

    @DeleteMapping("/deleteAdmin")
    public ResponseEntity<String> deleteAdminProfile(HttpServletRequest request){
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrueAndSuperadminTrue(mail);
        user.setActive(false);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("admin deleted successfully",headers, HttpStatus.OK);

    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(authenticateService.authenticatePassword(passwordDTO.getOldPassword(),request)){
            if (passwordDTO.getNewPassword1().equals(passwordDTO.getNewPassword2())){
                user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword1()));
                userRepository.save(user);
                return new ResponseEntity("password changed successfully",headers, HttpStatus.OK);
            }else {
                return new ResponseEntity("new passwords are not the same",headers, HttpStatus.OK);
            }
        }else {
            return new ResponseEntity("old passwords are not the same",headers, HttpStatus.OK);
        }


    }




}
