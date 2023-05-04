package com.example.solfamidasback.service;

import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.ExternalMusician;
import com.example.solfamidasback.model.Formation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AbsenceService {

    public Integer moneyAbsence(Formation formation){
        List<CalendarEvent> calendarEvents = formation.getCalendarEvents();
        Double amountTotal = 0.0;
        Double amountSpentExternalMusician = 0.0;

        List<CalendarEvent> calendarEventsPaid = new ArrayList<>();

        for ( CalendarEvent calendarEvent : calendarEvents){
            if (calendarEvent.isPaid()){
                calendarEventsPaid.add(calendarEvent);
            }
        }
        for (CalendarEvent calendPaid : calendarEventsPaid){
            for (ExternalMusician externalMusician : calendPaid.getExtenalMusicianList()){
                amountSpentExternalMusician = amountSpentExternalMusician + externalMusician.getAmount();
            }
            amountTotal = amountTotal + calendPaid.getAmount();
        }

        amountTotal= amountTotal - amountSpentExternalMusician;

        return null;
    }
}
