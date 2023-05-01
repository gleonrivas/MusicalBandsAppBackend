package com.example.solfamidasback.controller;

import com.example.solfamidasback.configSecurity.RegisterRequest;
import com.example.solfamidasback.controller.DTO.CalendarEventDTO;
import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.CalendarEventService;
import com.example.solfamidasback.service.JwtService;
import io.jsonwebtoken.security.SignatureException;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    UserRepository userRepository;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;


    @Operation(summary = "Create a calendar event for the formation",
            description = "The calendar event is for one formation and is created by director, president or assistance controller",
            tags = {"token"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @PostMapping("CreateEvents")
    public ResponseEntity<CalendarEvent> createCalendarEvent(@RequestBody CalendarEventDTO calendarEventDTO,
                                                             HttpServletRequest request){


        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Error de token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        if (!calendarEventService.comprobarjwt(jwtToken)) {
            String mensaje = "Error de token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }
        String mail =  jwtService.extractUsername(jwtToken);
        System.out.println(mail);
        Users user = userRepository.findByEmailAndActiveTrue(mail);


        CalendarEvent calendarEvent = new CalendarEvent();
        if(calendarEventService.VerifyCalendarEventDTO(calendarEventDTO)) {
            boolean pagado = false;
            if (calendarEventDTO.getPaid().contains("1")){ pagado = true ;};

            var formation = formationRepository.findById(Integer.valueOf(calendarEventDTO.getIdFormation()));
            calendarEvent.setType(calendarEventDTO.getEnumTypeActuation().toString());
            calendarEvent.setDate(LocalDate.parse(calendarEventDTO.getDate()));
            calendarEvent.setAmount(Double.parseDouble(calendarEventDTO.getAmount()));
            calendarEvent.setDescription(calendarEventDTO.getDescription());
            calendarEvent.setPaid(pagado);
            calendarEvent.setPlace(calendarEventDTO.getPlace());
            calendarEvent.setTitle(calendarEventDTO.getTitle());
            calendarEvent.setFormation(formation.get());

//            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }else{
            String mensaje = "Hay errores en el formulario";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }
    }
    @Operation(summary = "Retrieve a list of calendar events for the user",
            description = "The response is a list of Formation Objects",
            tags = {"token"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("MyEvents")
    public ResponseEntity<List<CalendarEvent>> listMyEvents(@RequestParam Integer formationId){

        return ResponseEntity.ok(calendarEventRepository.findAll().stream().filter(idformation->idformation.getFormation().getId().equals(formationId)).toList());
    }

}
