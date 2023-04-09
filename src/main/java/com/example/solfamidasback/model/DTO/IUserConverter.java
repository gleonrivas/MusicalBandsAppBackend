package com.example.solfamidasback.model.DTO;

import com.example.solfamidasback.model.User;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface IUserConverter {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);
    static String mapLocalDateTimeToString(LocalDateTime localDateTime){return LocalDateTime.MIN.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));}


}
