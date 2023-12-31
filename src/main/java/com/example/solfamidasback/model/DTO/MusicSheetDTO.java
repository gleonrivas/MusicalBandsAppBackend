package com.example.solfamidasback.model.DTO;

import com.example.solfamidasback.model.Users;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicSheetDTO {

    private Integer id;

    private String musicSheetPdf;

    private Integer formationId;

    private Integer userId;



}
