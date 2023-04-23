package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.CalendarEventDTO;
import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.service.CalendarEventService;
import com.example.solfamidasback.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Tag(name = "Calendar_Event", description = "Calendar crud")
@RestController
@RequestMapping("/calendar")
public class CalendarEventController {

    @Autowired
    CalendarEventRepository calendarEventRepository;

    @Autowired
    CalendarEventService calendarEventService;

    @Autowired
    FormationRepository formationRepository;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;


    @Operation(summary = "Create a calendar event for the formation",
            description = "The calendar event is for one formation and is created by director, president or assistance controller",
            tags = {"token"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("CreateEvents")
    public ResponseEntity<CalendarEvent> createCalendarEvent(@RequestBody CalendarEventDTO calendarEventDTO){
        CalendarEvent calendarEvent = new CalendarEvent();

        var formation = formationRepository.findById(calendarEventDTO.getIdFormation());
        calendarEvent.setType(calendarEventDTO.getEnumTypeActuation().toString());
        calendarEvent.setDate(calendarEventDTO.getDate());
        calendarEvent.setAmount(calendarEventDTO.getAmount());
        calendarEvent.setDescription(calendarEventDTO.getDescription());
        calendarEvent.setPaid(calendarEventDTO.isPaid());
        calendarEvent.setPlace(calendarEventDTO.getPlace());
        calendarEvent.setTitle(calendarEventDTO.getTitle());
        calendarEvent.setFormation(formation.get());


        return ResponseEntity.ok(calendarEvent);
    }
    @Operation(summary = "Retrieve a list of calendar events for the user",
            description = "The response is a list of Formation Objects",
            tags = {"token"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("MyEvents")
    public ResponseEntity<List<CalendarEvent>> listMyEvents(HttpServletRequest request){
        List<CalendarEvent> calendarEventList = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        return new ResponseEntity(calendarEventList,httpHeaders, HttpStatus.OK);
    }

}
