package com.example.solfamidasback.controller.DTO;
import com.example.solfamidasback.model.Enums.EnumTypeActuation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CalendarEventDTO {
    private Integer idFormation;
    private EnumTypeActuation enumTypeActuation;
    private String title;
    private String place;
    private boolean paid;
    private String description;
    private LocalDate date;
    private Double amount;
}
