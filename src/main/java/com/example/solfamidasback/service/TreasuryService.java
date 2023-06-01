package com.example.solfamidasback.service;

import com.example.solfamidasback.controller.DTO.PayFormationDTO;
import com.example.solfamidasback.controller.DTO.UserUpdateDTO;
import com.example.solfamidasback.controller.DTO.UsersPaidDTO;
import com.example.solfamidasback.model.*;
import com.example.solfamidasback.model.DTO.UserPaidDTO;
import com.example.solfamidasback.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
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
    @Autowired
    CalendarEventRepository calendarEventRepository;

    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }

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
        List<CalendarEvent> listCalendarEvents = calendarEventRepository.getCalendarFormation(formation.getId());
        List<CalendarEvent> calendarEventsPaid = new ArrayList<>();
        for (CalendarEvent calendarEvent : listCalendarEvents){
            if (calendarEvent.getAmount() > 0){
                calendarEventsPaid.add(calendarEvent);
            }
        }

        return  calendarEventsPaid;
    }

    public List<ExternalMusician> externalMusicianEvents (Formation formation){
        List<CalendarEvent> listCalendarEvents = calendarEventRepository.getCalendarFormation(formation.getId());;
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


        treasuryNew.setAmount(formatearDecimales((treasuryLast.getAmount() - externalMusician.getAmount()),2));
        treasuryNew.setFormation(treasuryLast.getFormation());
        treasuryNew.setReceiveMoneyDate(LocalDate.now());
        treasuryNew.setActive(true);
        treasuryRepository.save(treasuryNew);
    }

    public void calendarEventPaid (CalendarEvent calendarEvent){
        Double amount = calendarEvent.getAmount();

        Treasury treasuryLast = treasuryRepository.findLastTreasury();
        Treasury treasuryNew = new Treasury();

        treasuryNew.setAmount(formatearDecimales((treasuryLast.getAmount() + amount),2));
        treasuryNew.setFormation(treasuryLast.getFormation());
        treasuryNew.setReceiveMoneyDate(LocalDate.now());
        treasuryNew.setActive(true);
        treasuryRepository.save(treasuryNew);
        calendarEvent.setPaid(true);

        calendarEventRepository.save(calendarEvent);
    }

    public UserPaidDTO paidUserFormation (Users users,Formation formation){
        UserPaidDTO userPaidDTO = new UserPaidDTO();
        //Buscamos todas las ausencias que tiene el usuario
        List<Absence> absenceList = absenceRepository.getAllByIdUser(users.getId());
        Double  totalPenalty = 0.0;
        //De todas las ausencias calculamos el porcentaje que se quita del ensayo o bolo
        for (Absence absence : absenceList){
            Double amount = absence.getCalendar().getAmount();
            Double penalty = absence.getCalendar().getPenaltyPonderation();
            Double total = (amount * penalty) / 100;
            totalPenalty = total++;
        }

        //Obtenemos la cantidad total que tiene la agrupación
        Treasury treasuryLast = treasuryRepository.findLastTreasuryPay(formation.getId());
        Double amountTotal = treasuryLast.getAmount();

        //Obtenemos la cantidad total de miembros que tiene la agrupación
        Integer totalUsers = userFormationRoleRepository.countUsersFormation(formation.getId());

        //Calculamos cuanto le pertenece al usuario
        Double howMuchBelong = amountTotal / totalUsers;
        Double howMuchBelongPenalty = howMuchBelong - totalPenalty;

        //Se crea un nuevo registro  descontandose lo que se le ha pagado
        Treasury treasuryNew = new Treasury();
        treasuryNew.setAmount(formatearDecimales((treasuryLast.getAmount() - howMuchBelongPenalty),2));
        treasuryNew.setFormation(treasuryLast.getFormation());
        treasuryNew.setReceiveMoneyDate(LocalDate.now());
        treasuryNew.setActive(true);
        treasuryRepository.save(treasuryNew);



        userPaidDTO.setName(users.getName());
        userPaidDTO.setSurname(userPaidDTO.getSurname());
        userPaidDTO.setPenalty(formatearDecimales(totalPenalty,2));
        userPaidDTO.setAmount(formatearDecimales(howMuchBelongPenalty,2));

        //Se pone inactivo ya que se da de baja de la formación
        users.setActive(false);
        userRepository.save(users);


        return userPaidDTO;
    }


    public UsersPaidDTO usersPenalty (Users user){
        //Buscamos todas las ausencias que tiene el usuario
        List<Absence> absenceList = absenceRepository.getAllByIdUser(user.getId());
        Double totalPenalty = 0.0;
        //De todas las ausencias calculamos el porcentaje que se quita del ensayo o bolo
        for (Absence absence : absenceList){
            Double amount = absence.getCalendar().getAmount();
            Double penalty = absence.getCalendar().getPenaltyPonderation();
            Double total = (amount * penalty) / 100;
            totalPenalty = total++;
        }


        //Creamos un usuario DTO con la información que necesitamos
        UsersPaidDTO usersPaid = new UsersPaidDTO();
        usersPaid.setName(user.getName());
        usersPaid.setSubname(user.getSurName());
        usersPaid.setAmountPenalty(formatearDecimales(totalPenalty,2));

        return  usersPaid;
    }

    public PayFormationDTO calculatePaidFormation (Formation formation){
        //Obtenemos todos los usuarios activos que tiene la formación
        List<Integer> idsUsers = userFormationRoleRepository.getAllUsersIdFormation(formation.getId());
        List<Users> usersList = new ArrayList<>();
        for (Integer idUser : idsUsers){
            Users user = userRepository.findByIdAndActiveIsTrue(idUser);
            if (user != null){
                usersList.add(user);
            }

        }

        //Obtenemos la cantidad total que tiene la agrupación
        Treasury treasuryLast = treasuryRepository.findLastTreasuryPay(formation.getId());
        Double amountTotal = treasuryLast.getAmount();

        //Obtenemos con cuantos miembros tiene que repartir y la cantidad de la formación
        Integer totalUsers = usersList.size();

        Double amountTotalPerUsers = amountTotal / totalUsers;
        Double restAmount = 0.0;

        //Vemos cuanta penalización tiene cada usuario en total
        List<UsersPaidDTO> usersPaids = new ArrayList<>();
        for (Users user : usersList){
            UsersPaidDTO u = usersPenalty(user);
            u.setAmountReceibes(formatearDecimales((amountTotalPerUsers - u.getAmountPenalty()),2));
            usersPaids.add(u);
            Double totalRestAmount =  amountTotalPerUsers - u.getAmountPenalty();
            restAmount = formatearDecimales((restAmount + totalRestAmount),2);
        }

        //Se crea un nuevo registro  descontandose lo que se le ha pagado
        Treasury treasuryNew = new Treasury();
        treasuryNew.setAmount(formatearDecimales((treasuryLast.getAmount() - restAmount),2));
        treasuryNew.setFormation(treasuryLast.getFormation());
        treasuryNew.setReceiveMoneyDate(LocalDate.now());
        treasuryNew.setActive(true);
        treasuryRepository.save(treasuryNew);

        PayFormationDTO payFormationDTO = new PayFormationDTO();

        payFormationDTO.setUsersPaid(usersPaids);
        payFormationDTO.setPayDay(LocalDate.now());
        payFormationDTO.setInAccount(formatearDecimales((treasuryLast.getAmount() - restAmount),2) );
        payFormationDTO.setTotalPaid(restAmount);

         return payFormationDTO;
    }



}
