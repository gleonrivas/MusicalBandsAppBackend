package com.example.solfamidasback.utils;

import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utilities {

    @Autowired
    static
    UserRepository userRepository;

    public static Users getUserFromHeader(SecurityContextHolder securityContextHolder){
        Authentication authentication = securityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByEmailAndActiveIsTrue(authentication.getName());
        return user;
    }

}
