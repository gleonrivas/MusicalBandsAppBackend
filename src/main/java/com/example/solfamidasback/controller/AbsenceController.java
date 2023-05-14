package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.FormationIdDTO;
import com.example.solfamidasback.controller.DTO.RegisterAbsenceDTO;
import com.example.solfamidasback.controller.DTO.ResponseStringDTO;
import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.CalendarEventService;
import com.example.solfamidasback.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Absence", description = "Absence crud")
@RestController
@RequestMapping("/absence")
public class AbsenceController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CalendarEventRepository calendarEventRepository;

    @Autowired CalendarEventService calendarEventService;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Retrieve a list of calendar events for the user",
            description = "The response is a list of Calendar Event Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CalendarEvent.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("RegisterAbsence")
    public ResponseEntity<CalendarEvent> listAllMyEvents(@RequestBody RegisterAbsenceDTO registerAbsenceDTO, HttpServletRequest request) {


        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        } catch (Exception e) {
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Token error");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail = jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        // validacion de datos del body


        //validacion de que el usuario pertenece a la formacion
        CalendarEvent calendarEvent = calendarEventRepository.getReferenceById(Integer.parseInt(registerAbsenceDTO.getCalendarEventId()));
        if(!user.getFormationList().contains(calendarEvent.getFormation())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("It's not your formation");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }





        return null;
    }

}
