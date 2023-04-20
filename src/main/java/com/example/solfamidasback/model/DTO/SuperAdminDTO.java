package com.example.solfamidasback.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminDTO {


    private String name;

    private String surName;

    private String email;

    private String birthDate;

    private String dni;

    private boolean SuperAdmin;




}
