package com.example.solfamidasback.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    private String name;

    private String surName;

    private String email;

    private LocalDateTime birthDate;

    private String dni;



}
