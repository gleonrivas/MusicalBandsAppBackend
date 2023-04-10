package com.example.solfamidasback.controller.DTO;

import com.example.solfamidasback.model.Enums.EnumFormationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormationDTO {
 private Integer id_user;
 private String name;
 private String designation;
 private EnumFormationType type;
 private String fundationDate;
 private String logo;





}
