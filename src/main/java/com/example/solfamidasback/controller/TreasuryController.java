package com.example.solfamidasback.controller;


import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.DTO.PayLowDTO;
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

    @GetMapping("/getAllEvents/{formationId}")
    public ResponseEntity<List<CalendarEvent>> getAllEvents (@PathVariable Integer idFormation){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        List<CalendarEvent> calendarEventList = treasuryService.eventsWithMoney(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(calendarEventList,httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/getAllExternalMusician/{formationId}")
    public ResponseEntity<List<ExternalMusician>> getAllExternalMusician (@PathVariable Integer idFormation){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        List<ExternalMusician> externalMusicianList = treasuryService.externalMusicianEvents(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payMusician/{externalMusicianId}")
    public ResponseEntity<ExternalMusician> payExternalMusician(@PathVariable Integer externalMusicianId){
        ExternalMusician externalMusician = externalMusicianRepository.findExternalMusicianByIdAndActiveIsTrue(externalMusicianId);
        treasuryService.externalMusicianPaid(externalMusician);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("Se ha pagado al músico",httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payEvent/{calendarEventId}")
    public ResponseEntity<CalendarEvent> payEvent(@PathVariable Integer calendarEventId){
        CalendarEvent calendarEvent = calendarEventRepository.findCalendarEventById(calendarEventId);
        treasuryService.calendarEventPaid(calendarEvent);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("Se ha recibido el dinero del evento",httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/payLow")
    public ResponseEntity<Double> payLowUser(@NotNull @RequestBody PayLowDTO payLowDTO){
        Users users = userRepository.findByIdAndActiveIsTrue(payLowDTO.getUserId());
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        Double amount = treasuryService.paidUserFormation(users,formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(amount,httpHeaders, HttpStatus.OK);
    }

}
