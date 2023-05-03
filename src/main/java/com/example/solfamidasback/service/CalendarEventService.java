package com.example.solfamidasback.service;

import com.example.solfamidasback.controller.DTO.CalendarEventDTO;
import com.example.solfamidasback.model.Enums.EnumTypeActuation;
import com.nimbusds.jose.util.IntegerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
                comprobarFecha(calendarEventDTO.getDate())&&
                comprobarDouble(calendarEventDTO.getAmount())
        ){
            return true;
        }
        return false;
   }
   private boolean comprobarFecha(String fecha){
       try {
           LocalDate.parse(fecha);
       }catch (Exception e){
           return false;
       }
       return true;
   }
    private boolean comprobarDouble(String doble){
        try {
            Double.parseDouble(doble);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public boolean comprobarjwt(String jwttoken){

            try {
                String mail =  jwtService.extractUsername(jwttoken);
            }catch (Exception e){
                String mensaje = "Error de token";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return false;
            }
            return true;
        }
}

