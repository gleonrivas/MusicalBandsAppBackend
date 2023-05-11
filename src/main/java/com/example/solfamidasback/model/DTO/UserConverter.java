package com.example.solfamidasback.model.DTO;

import com.example.solfamidasback.model.Users;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserConverter  {

    public UserDTO toDTO(Users user){

        UserDTO userDTO = new UserDTO();

        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setSurName(user.getSurName());
        userDTO.setDni(user.getDni());
        if (user.getBirthDate()!=null) userDTO.setBirthDate(user.getBirthDate().toString());

        return userDTO;
    }

    public Users toEntity(UserDTO user){

        Users users = new Users();

        users.setName(user.getName());
        users.setEmail(user.getEmail());
        users.setSurName(user.getSurName());
        users.setDni(user.getDni());
        users.setBirthDate(LocalDate.parse(user.getBirthDate()).atStartOfDay());

        return users;
    }

    public Users fromAdmintoEntity(SuperAdminDTO user){

        Users users = new Users();

        users.setName(user.getName());
        users.setEmail(user.getEmail());
        users.setSurName(user.getSurName());
        users.setDni(user.getDni());
        users.setBirthDate(LocalDate.parse(user.getBirthDate()).atStartOfDay());
        users.setSuperadmin(user.isSuperAdmin());

        return users;
    }


}
