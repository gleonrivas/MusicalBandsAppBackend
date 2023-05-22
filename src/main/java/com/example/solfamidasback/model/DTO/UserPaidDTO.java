package com.example.solfamidasback.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPaidDTO {
    private String name;
    private String surname;
    private Double amount;
    private Double penalty;
}
