package com.example.solfamidasback.service;

import com.example.solfamidasback.model.*;
import com.example.solfamidasback.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TreasuryService {
    @Autowired
    ExternalMusicianRepository externalMusicianRepository;
    @Autowired
    TreasuryRepository treasuryRepository;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserFormationRoleRepository userFormationRoleRepository;

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

    public List<CalendarEvent> eventsWithMoney (Formation formation){
        List<CalendarEvent> listCalendarEvents = formation.getCalendarEvents();
        List<CalendarEvent> calendarEventsPaid = new ArrayList<>();
        for (CalendarEvent calendarEvent : listCalendarEvents){
            if (calendarEvent.getAmount() > 0){
                calendarEventsPaid.add(calendarEvent);
            }
        }

        return  calendarEventsPaid;
    }

    public List<ExternalMusician> externalMusicianEvents (Formation formation){
        List<CalendarEvent> listCalendarEvents = formation.getCalendarEvents();
        List<ExternalMusician> calendarExternalMusician = new ArrayList<>();
        for (CalendarEvent calendarEvent : listCalendarEvents){
            for (ExternalMusician externalMusician : calendarEvent.getExtenalMusicianList()){
                calendarExternalMusician.add(externalMusician);
            }
        }
        return calendarExternalMusician;
    }

    public void externalMusicianPaid (ExternalMusician externalMusician){
        externalMusician.setPaid(true);
        externalMusicianRepository.save(externalMusician);

        Treasury treasuryLast = treasuryRepository.findLastTreasury();
        Treasury treasuryNew = new Treasury();

        treasuryNew.setAmount(treasuryLast.getAmount() - externalMusician.getCalendar().getAmount());
        treasuryNew.setFormation(treasuryLast.getFormation());
        treasuryNew.setReceiveMoneyDate(LocalDate.now());
        treasuryNew.setActive(true);
        treasuryRepository.save(treasuryNew);
    }

    public void calendarEventPaid (CalendarEvent calendarEvent){
        Double amount = calendarEvent.getAmount();

        Treasury treasuryLast = treasuryRepository.findLastTreasury();
        Treasury treasuryNew = new Treasury();

        treasuryNew.setAmount(treasuryLast.getAmount() + amount);
        treasuryNew.setFormation(treasuryLast.getFormation());
        treasuryNew.setReceiveMoneyDate(LocalDate.now());
        treasuryNew.setActive(true);
        treasuryRepository.save(treasuryNew);
    }

    public Double paidUserFormation (Users users,Formation formation){
        //Buscamos todas las ausencias que tiene el usuario
        List<Absence> absenceList = absenceRepository.getAllByIdUser(users.getId());
        Double totalPenalty = 0.0;
        //De todas las ausencias calculamos el porcentaje que se quita del ensayo o bolo
        for (Absence absence : absenceList){
            Double amount = absence.getCalendar().getAmount();
            Double penalty = absence.getCalendar().getPenaltyPonderation();
            Double total = (amount * penalty) / 100;
            totalPenalty = total++;
        }

        //Obtenemos la cantidad total que tiene la agrupación
        Treasury treasuryLast = treasuryRepository.findLastTreasury(formation.getId());
        Double amountTotal = treasuryLast.getAmount();

        //Obtenemos la cantidad total de miembros que tiene la agrupación
        Integer totalUsers = userFormationRoleRepository.countUsersFormation(formation.getId());

        //Calculamos cuanto le pertenece al usuario
        Double howMuchBelong = amountTotal / totalUsers;
        Double howMuchBelongPenalty = howMuchBelong - totalPenalty;

        //Se crea un nuevo registro  descontandose lo que se le ha pagado
        Treasury treasuryNew = new Treasury();
        treasuryNew.setAmount(treasuryLast.getAmount() - howMuchBelongPenalty);
        treasuryNew.setFormation(treasuryLast.getFormation());
        treasuryNew.setReceiveMoneyDate(LocalDate.now());
        treasuryNew.setActive(true);
        treasuryRepository.save(treasuryNew);


        return howMuchBelongPenalty;
    }


}
