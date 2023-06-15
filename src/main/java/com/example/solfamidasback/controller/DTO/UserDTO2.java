package com.example.solfamidasback.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO2 {

    private Integer id;

    private String name;

    private String surName;

    private String email;

    private String birthDate;

    private String dni;

    private String url;



}