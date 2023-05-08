package com.example.solfamidasback.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalMusicianDTO {
    private Integer amount;
    private String dni;
    private String name;
    private String surname;
    private Integer idCalendar;
    private String bankAccount;
    private String email;
    private String phone;

}
