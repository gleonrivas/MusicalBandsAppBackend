package com.example.solfamidasback.controller.DTO;

import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO2 {
    private Integer id;
    private EnumRolUserFormation type;

}