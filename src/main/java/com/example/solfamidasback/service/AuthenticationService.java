package com.example.solfamidasback.service;

import com.example.solfamidasback.configSecurity.AuthenticationRequests;
import com.example.solfamidasback.configSecurity.AuthenticationResponses;
import com.example.solfamidasback.configSecurity.RegisterRequest;
import com.example.solfamidasback.model.Enums.EnumRolAuth;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    public AuthenticationResponses register(RegisterRequest request) {

        var user = Users.builder()
                .name(request.getFirstname())
                .surName(request.getSecondname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .enumRolAuth(EnumRolAuth.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        String mostrar = "Usuario registrado con éxito";
        String token = "Logueate para ver tu token";
        return AuthenticationResponses.builder()
                .resultado(mostrar)
                .token(token)
                .build();
    }

    public AuthenticationResponses authenticate(AuthenticationRequests request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

       var users = userRepository.findByEmail(request.getEmail());

       if (encoder.matches(request.getPassword(), users.get().getPassword()) && users.get().getEmail().equals(request.getEmail())){

       var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        String mostrar = "Autenticado con éxito";
        return AuthenticationResponses.builder()
                .token(jwtToken)
                .resultado(mostrar)
                .build();
        } else {

           return AuthenticationResponses.builder().build();
       }

    }

    public Boolean authenticatePassword(String password, HttpServletRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users users = userRepository.findByEmailAndActiveTrue(mail);

        if (encoder.matches(password, users.getPassword())){
            return true;
        } else {
            return false;
        }

    }

}
