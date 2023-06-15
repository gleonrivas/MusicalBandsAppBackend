package com.example.solfamidasback.controller.DTO;

import com.example.solfamidasback.model.DTO.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersFormationRoleDTO2 {

    private Integer id;
    private String name;

    private String image;

    private String surName;

    private String email;

    private String birthDate;

    private List<RoleDTO2> roleDTOList;


}