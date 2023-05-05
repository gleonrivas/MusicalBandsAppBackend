package com.example.solfamidasback.controller.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowedMaterialUpdateDTO {
    Integer materialId;
    Integer userId;
    Integer newMaterialId;
    Integer newUserId;
}
