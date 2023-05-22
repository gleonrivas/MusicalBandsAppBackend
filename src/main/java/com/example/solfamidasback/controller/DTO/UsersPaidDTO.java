package com.example.solfamidasback.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersPaidDTO {
    private String name;
    private String subname;
    private Double amountPenalty;
    private Double amountReceibes;
}
