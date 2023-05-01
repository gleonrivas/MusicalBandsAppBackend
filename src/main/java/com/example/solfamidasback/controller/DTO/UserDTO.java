package com.example.solfamidasback.controller.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

    private String name;

    private String surName;

    private String email;

    private String birthDate;

    private String dni;

    private Boolean superadmin;

    private String password;

}
