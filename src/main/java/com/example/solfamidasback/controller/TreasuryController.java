package com.example.solfamidasback.controller;


import com.example.solfamidasback.controller.DTO.PayFormationDTO;
import com.example.solfamidasback.controller.DTO.UsersPaidDTO;
import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.DTO.CalendarDTO;
import com.example.solfamidasback.model.DTO.ExternalMusicianDTO;
import com.example.solfamidasback.model.DTO.PayLowDTO;
import com.example.solfamidasback.model.DTO.UserPaidDTO;
import com.example.solfamidasback.model.ExternalMusician;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.*;
import com.example.solfamidasback.service.TreasuryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Treasury", description = "Treasury logic")
@RestController
@RequestMapping("/treasury")
public class TreasuryController {
    @Autowired
    TreasuryRepository treasuryRepository;
    @Autowired
    TreasuryService treasuryService;
    @Autowired
    FormationRepository formationRepository;
    @Autowired
    ExternalMusicianRepository externalMusicianRepository;
    @Autowired
    CalendarEventRepository calendarEventRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/getAllEvents")
    public ResponseEntity<List<CalendarEvent>> getAllEvents (@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation == null){
            List<CalendarEvent> calendarEventList = new ArrayList<>();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(calendarEventList,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        List<CalendarEvent> calendarEventList = treasuryService.eventsWithMoney(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(calendarEventList,httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/getAllExternalMusician")
    public ResponseEntity<List<ExternalMusician>> getAllExternalMusician (@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation == null){
            List<ExternalMusician> externalMusicians = new ArrayList<>();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(externalMusicians,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        List<ExternalMusician> externalMusicianList = treasuryService.externalMusicianEvents(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payMusician")
    public ResponseEntity<ExternalMusician> payExternalMusician(@RequestBody ExternalMusicianDTO externalMusicianDTO){
        ExternalMusician externalMusician = externalMusicianRepository.findExternalMusicianByIdAndActiveIsTrue(externalMusicianDTO.getExternalMusicianId());
        if (externalMusician == null){
            ExternalMusician externalMusicians = new ExternalMusician();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(externalMusicians,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        treasuryService.externalMusicianPaid(externalMusician);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("Se ha pagado al músico",httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payEvent")
    public ResponseEntity<CalendarEvent> payEvent(@RequestBody CalendarDTO calendarDTO){
        CalendarEvent calendarEvent = calendarEventRepository.findCalendarEventById(calendarDTO.getCalendarId());
        treasuryService.calendarEventPaid(calendarEvent);
        if (calendarEvent == null){
            CalendarEvent calendarEvent1 = new CalendarEvent();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(calendarEvent1,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("Se ha recibido el dinero del evento",httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payLow")
    public ResponseEntity<UserPaidDTO> payLowUser(@NotNull @RequestBody PayLowDTO payLowDTO){
        Users users = userRepository.findByIdAndActiveIsTrue(payLowDTO.getUserId());
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (users == null || formation == null){
            UserPaidDTO userPaidDTO = new UserPaidDTO();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(userPaidDTO,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        UserPaidDTO userPaid = treasuryService.paidUserFormation(users,formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(userPaid,httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payFormation")
    public ResponseEntity<PayFormationDTO> payFormation(@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation == null){
            PayFormationDTO payFormationDTO = new PayFormationDTO();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(payFormationDTO,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        PayFormationDTO payFormationDTO = treasuryService.calculatePaidFormation(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(payFormationDTO,httpHeaders, HttpStatus.OK);
    }
}
