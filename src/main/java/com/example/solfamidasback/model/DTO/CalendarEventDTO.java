package com.example.solfamidasback.model.DTO;
import com.example.solfamidasback.model.Enums.EnumTypeActuation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CalendarEventDTO {
    private String idFormation;  //Integer
    private String enumTypeActuation; //EnumTypeActuation
    private String title;
    private String place;
    private String paid; //boolean
    private String description;
    private String date;  //LocalDate
    private String amount;  //Double
    private String penaltyPonderation; //Double
}
