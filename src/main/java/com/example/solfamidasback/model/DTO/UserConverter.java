package com.example.solfamidasback.model.DTO;

import com.example.solfamidasback.model.User;
import com.example.solfamidasback.service.UserService;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public abstract class UserConverter  {
    @Autowired
    UserService userService;

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surName", target = "surName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "dni", target = "dni")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "superadmin", target = "superadmin")
    public abstract User toEntity(UserDTO userDTO);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "surName", target = "surName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "LocalDateTimeToString")
    @Mapping(source = "dni", target = "dni")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "superadmin", target = "superadmin")
    @Mapping(source = "name", target = "name")
    public abstract UserDTO toDTO(User user);

    @Named("LocalDateTimeToString")
    public String mapLocalDateTimeToString(LocalDateTime localDateTime){return LocalDateTime.MIN.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));}


}
