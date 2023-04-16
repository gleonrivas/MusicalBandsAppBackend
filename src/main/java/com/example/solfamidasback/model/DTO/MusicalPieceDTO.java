package com.example.solfamidasback.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicalPieceDTO {

    private String name;
    private String author;
    private Double length;
}
