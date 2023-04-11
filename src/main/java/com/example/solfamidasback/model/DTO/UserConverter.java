package com.example.solfamidasback.model.DTO;

import com.example.solfamidasback.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserConverter  {

    public UserDTO toDTO(Users user){

        UserDTO userDTO = new UserDTO();

        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setSurName(user.getSurName());
        userDTO.setDni(user.getDni());
        userDTO.setBirthDate(user.getBirthDate());

        return userDTO;
    }

}
