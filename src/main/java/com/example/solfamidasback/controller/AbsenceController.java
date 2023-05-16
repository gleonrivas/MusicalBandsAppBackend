package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.CalendarEventIdDTO;
import com.example.solfamidasback.controller.DTO.FormationIdDTO;
import com.example.solfamidasback.controller.DTO.RegisterAbsenceDTO;
import com.example.solfamidasback.controller.DTO.ResponseStringDTO;
import com.example.solfamidasback.model.Absence;
import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.DTO.UserDTO;
import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.AbsenceRepository;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.UserFormationRoleRepository;
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
import com.example.solfamidasback.model.DTO.UserConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Tag(name = "Absence", description = "Absence crud")
@RestController
@RequestMapping("/absence")
public class AbsenceController {
    @Autowired UserRepository userRepository;

    @Autowired CalendarEventRepository calendarEventRepository;

    @Autowired CalendarEventService calendarEventService;

    @Autowired UserFormationRoleRepository userFormationRoleRepository;

    @Autowired AbsenceRepository absenceRepository;

    @Autowired UserConverter userConverter;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Save a list of user who are absence on a event",
            description = "Save a list of user who are absence on a event by calendar event id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Absence.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("RegisterAbsence")
    public ResponseEntity<ResponseStringDTO> listAllMyEvents(@RequestBody RegisterAbsenceDTO registerAbsenceDTO, HttpServletRequest request) {

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

        if(!calendarEventService.verifyInteger(registerAbsenceDTO.getCalendarEventId())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Data error");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }
        for (String s:registerAbsenceDTO.getListOfUserId()){
            if(!calendarEventService.verifyInteger(s)){
                ResponseStringDTO responseStringDTO = new ResponseStringDTO("Data error");
                return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
            }
        }
        CalendarEvent calendarEvent = calendarEventRepository.getReferenceById(Integer.parseInt(registerAbsenceDTO.getCalendarEventId()));

        // validar que los usuarios no esten ya registrados
        List<Absence> absenceList = absenceRepository.findAll().stream().filter(absence -> absence.getCalendar().getId().equals(calendarEvent.getId())).toList();
        if(!absenceList.isEmpty()){

        }


        //validar que no se repite ningun usuario

        for(int i=1,z=0;i<=registerAbsenceDTO.getListOfUserId().size()-1;i++){
            if(i<=z)i=z+1;
            if(Integer.parseInt(registerAbsenceDTO.getListOfUserId().get(i))==Integer.parseInt(registerAbsenceDTO.getListOfUserId().get(z))){
                ResponseStringDTO responseStringDTO = new ResponseStringDTO("There are repeated users");
                return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
            }
            if(i==registerAbsenceDTO.getListOfUserId().size()-1)z++;
            if(i==registerAbsenceDTO.getListOfUserId().size()-1)i=z;

        }


        //verificar que el evento es hoy
        if (!calendarEvent.getDate().equals(LocalDate.now())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("The event is not today");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }

        // validacion de que el usuario que registra pertenece a la formacion del evento y su rol es correcto
        if(!user.getFormationList().contains(calendarEvent.getFormation())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You do not belong to that formation");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }
        List<UserFormationRole> formationRoleList = user.getUserFormationRole().stream().filter(userFormationRole ->
                        userFormationRole.getFormation().getId()==calendarEvent.getFormation().getId())
                .collect(Collectors.toList()).stream().filter(userFormationRole
                        -> userFormationRole.getRole().getType().equals(EnumRolUserFormation.OWNER)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.ASSISTANCE_CONTROL)).collect(Collectors.toList());

        if (formationRoleList.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You can't register absences");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        //validar que los usuarios introducidos pertenecen a la formacion
        for (String s: registerAbsenceDTO.getListOfUserId()){
            Users  u = userRepository.getById(Integer.parseInt(s));
            List<UserFormationRole> userFormationRoles = u.getUserFormationRole().stream().filter(userFormationRole ->
                    userFormationRole.getFormation().equals(calendarEvent.getFormation())).collect(Collectors.toList());

            if(userFormationRoles.isEmpty()){
                ResponseStringDTO responseStringDTO = new ResponseStringDTO("There is a user who does not belong to the formation");
                return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
            }
        }

        //guardamos los registros en bbdd

        for(String s:registerAbsenceDTO.getListOfUserId()){
            Absence absence = new Absence();
            absence.setCalendar(calendarEvent);
            absence.setFullDate(LocalDateTime.now());
            absence.setUsers(userRepository.getReferenceById(Integer.parseInt(s)));
            absenceRepository.save(absence);

        }
        return ResponseEntity.ok(new ResponseStringDTO("Success"));
    }

    @Operation(summary = "Retrieve a lis of user who are absence on a event",
            description = "Save a list of user who are absence on a event by calendar event id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Absence.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("SeeUsersAbsense")
    public ResponseEntity<List<UserDTO>> usersAbsenceOnCalendarEvent(@RequestBody CalendarEventIdDTO calendarEventIdDTO, HttpServletRequest request) {

        List<Absence> absenceList = new ArrayList<>();
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

        //sanear la entrada
        if (!calendarEventService.verifyInteger(calendarEventIdDTO.getCalendarEventId())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Form error");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }

        //verificar que el usuario pertenece a la formacion
        CalendarEvent calendarEvent = calendarEventRepository.getReferenceById(Integer.parseInt(calendarEventIdDTO.getCalendarEventId()));
        List<UserFormationRole> userFormationRole = user.getUserFormationRole().stream().filter(userFormationRole1 ->
                userFormationRole1.getFormation().equals(calendarEvent.getFormation())).toList();
        if (userFormationRole.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You do not belong to that formation");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }

        //buscar los ausentes a ese dia
        List<Absence> absences = absenceRepository.findAll().stream().filter(absence -> absence.getCalendar().getId().equals(calendarEvent.getId())).toList();

        if (absences.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Nobody absence");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }

        List<UserDTO> userDTOList = new ArrayList<>();
        for (Absence a:absences){
            UserDTO userDTO = userConverter.toDTO(a.getUsers());
            userDTOList.add(userDTO);
        }
        return ResponseEntity.ok(userDTOList);
    }
}
