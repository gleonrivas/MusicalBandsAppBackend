package com.example.solfamidasback.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    private String name;

    private String surName;

    private String email;

    private String birthDate;

    private String dni;

    private Boolean superadmin;

    private String password;

}
