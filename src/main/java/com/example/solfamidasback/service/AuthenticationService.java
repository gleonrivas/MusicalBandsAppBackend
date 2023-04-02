package com.example.solfamidasback.service;

import com.example.solfamidasback.configSecurity.AuthenticationRequests;
import com.example.solfamidasback.configSecurity.AuthenticationResponses;
import com.example.solfamidasback.configSecurity.RegisterRequest;
import com.example.solfamidasback.model.Enums.EnumRolAuth;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponses register(RegisterRequest request) {
        var user = Users.builder()
                .name(request.getFirstname())
                .surName(request.getSecondname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enumRolAuth(EnumRolAuth.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponses.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponses authenticate(AuthenticationRequests request) {
        return null;
    }
}
