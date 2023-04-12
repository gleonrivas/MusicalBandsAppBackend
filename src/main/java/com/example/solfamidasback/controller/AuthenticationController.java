package com.example.solfamidasback.controller;

import com.example.solfamidasback.configSecurity.AuthenticationRequests;
import com.example.solfamidasback.configSecurity.AuthenticationResponses;
import com.example.solfamidasback.configSecurity.RegisterRequest;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Login", description = "Login crud")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor

public class AuthenticationController {


    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public static boolean validPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(pattern);
    }
    @Operation(summary = "Register new user",
            description = "Register new user by ",
            tags = {"firstname","secondname","email","password"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponses> register(
            @RequestBody RegisterRequest request) {
        if (request.getEmail().isBlank() || request.getPassword().isBlank() || request.getFirstname().isBlank() || request.getSecondname().isBlank()) {
            String mensaje = "Campos mal puestos o email no válido";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }
        if (!validPassword(request.getPassword())){
            String mensaje = "La contraseña tiene que tener al menos 8 caracteres y contener al menos un dígito, una letra minúscula y una letra mayúscula";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }

        if (!request.getEmail().contains("@") || !request.getEmail().contains(".")){
            String mensaje = "Formato de email no válido";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            String mensaje = "Email ya registrado";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Login by email and password",
            description = "Authenticate by email and password and response jswon with token",
            tags = {"email","password"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponses> authenticate(
            @RequestBody AuthenticationRequests request
    ){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        var users = userRepository.findByEmail(request.getEmail());

        if (!validPassword(request.getPassword())){
            String mensaje = "La contraseña tiene que tener al menos 8 caracteres y contener al menos un dígito, una letra minúscula y una letra mayúscula";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }

        if (!request.getEmail().contains("@") || !request.getEmail().contains(".")){
            String mensaje = "Formato de email no válido";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }

        if (request.getEmail().isBlank() || request.getPassword().isBlank()) {
            String mensaje = "Campos mal puestos o email no válido";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }
        if (!userRepository.existsByEmail(request.getEmail())) {
            String mensaje = "El email no existe";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }



        if (!encoder.matches(request.getPassword(), users.get().getPassword())){
            String mensaje = "La contraseña es incorrecta para el email usado";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);

        }

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }
}
