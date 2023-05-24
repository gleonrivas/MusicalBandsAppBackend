package com.example.solfamidasback.model.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MusicalPieceDTO {

    private Integer id;
    private String name;
    private String author;
    private Double length;
    private Integer idRepertory;

}
