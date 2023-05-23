package com.example.solfamidasback.utilities;

import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserFormationRoleRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;

public class Utilities {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";


    public static String createLink(){
        String link = java.util.UUID.randomUUID().toString();
        link.replaceAll("-", "");
        link.substring(0, 32);
        return link;
    }

    public static boolean isSuperAdministrador( HttpServletRequest request, JwtService jwtService, UserRepository userRepository){

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        if (user.getSuperadmin()){
            return true;
        }else {
            return false;
        }
    }

    public static EnumRolUserFormation getRoleUser(HttpServletRequest request, JwtService jwtService, UserRepository userRepository, Formation formation, UserFormationRoleRepository userFormationRoleRepository){
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        UserFormationRole userFormationRole = userFormationRoleRepository.getRoleByFormantionAndUser(formation.getId(), user.getId());
        return userFormationRole.getRole().getType();
    }
}
