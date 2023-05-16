package com.example.solfamidasback.controller;


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
        List<CalendarEvent> calendarEventList = treasuryService.eventsWithMoney(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(calendarEventList,httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/getAllExternalMusician")
    public ResponseEntity<List<ExternalMusician>> getAllExternalMusician (@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        List<ExternalMusician> externalMusicianList = treasuryService.externalMusicianEvents(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payMusician")
    public ResponseEntity<ExternalMusician> payExternalMusician(@RequestBody ExternalMusicianDTO externalMusicianDTO){
        ExternalMusician externalMusician = externalMusicianRepository.findExternalMusicianByIdAndActiveIsTrue(externalMusicianDTO.getExternalMusicianId());
        treasuryService.externalMusicianPaid(externalMusician);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("Se ha pagado al músico",httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payEvent")
    public ResponseEntity<CalendarEvent> payEvent(@RequestBody CalendarDTO calendarDTO){
        CalendarEvent calendarEvent = calendarEventRepository.findCalendarEventById(calendarDTO.getCalendarId());
        treasuryService.calendarEventPaid(calendarEvent);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("Se ha recibido el dinero del evento",httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payLow")
    public ResponseEntity<UserPaidDTO> payLowUser(@NotNull @RequestBody PayLowDTO payLowDTO){
        Users users = userRepository.findByIdAndActiveIsTrue(payLowDTO.getUserId());
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        UserPaidDTO userPaid = treasuryService.paidUserFormation(users,formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(userPaid,httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payFormation")
    public ResponseEntity<List<UsersPaidDTO>> payFormation(@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        List<UsersPaidDTO> usersPaid = treasuryService.calculatePaidFormation(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(usersPaid,httpHeaders, HttpStatus.OK);
    }
}
