package com.example.solfamidasback.controller.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAbsenceDTO {
    private String calendarEventId;
    private List<String> listOfUserId;
}
