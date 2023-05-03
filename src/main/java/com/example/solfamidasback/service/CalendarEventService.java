package com.example.solfamidasback.service;

import com.example.solfamidasback.model.DTO.CalendarEventDTO;
import com.example.solfamidasback.model.Formation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalendarEventService {
    @Autowired
    private JwtService jwtService;

    public boolean VerifyCalendarEventDTO(CalendarEventDTO calendarEventDTO){
        String regexid = "\\d+"; //expresion regular para verificar si es un digito
        String regextype = "PRACTICE|CONCERT";
        String regexpaid = "0|1";
        if(calendarEventDTO.getIdFormation().matches(regexid)&&
                calendarEventDTO.getEnumTypeActuation().matches(regextype)&&
                calendarEventDTO.getPaid().matches(regexpaid)&&
                verifyDate(calendarEventDTO.getDate())&&
                verifyDouble(calendarEventDTO.getAmount())
        ){
            return true;
        }
        return false;
   }
   public boolean verifyDate(String fecha){
       try {
           LocalDate.parse(fecha);
       }catch (Exception e){
           return false;
       }
       return true;
   }
    public boolean verifyDouble(String doble){
        try {
            Double.parseDouble(doble);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public boolean verifyInteger(String integer){
        try {
            Integer.parseInt(integer);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public boolean verifyFormation(List<Formation> formationList,Integer idFormation){
        for (Formation formation:formationList){
            if(formation.getId()==idFormation){
                return true;
            }
        }
        return false;

    }
}

