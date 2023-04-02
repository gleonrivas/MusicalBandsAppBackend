package com.example.solfamidasback.controller;

import com.example.solfamidasback.configSecurity.AuthenticationRequests;
import com.example.solfamidasback.configSecurity.AuthenticationResponses;
import com.example.solfamidasback.configSecurity.RegisterRequest;
import com.example.solfamidasback.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponses> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponses> authenticate(
            @RequestBody AuthenticationRequests request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));

    }
}
