package com.example.solfamidasback.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEventUpdateDTO{
    private String idCalendarEvent;
    private String enumTypeActuation; //EnumTypeActuation
    private String title;
    private String place;
    private String paid; //boolean
    private String description;
    private String date;  //LocalDate
    private String amount;  //Double
    private String penaltyPonderation; //Double
}
