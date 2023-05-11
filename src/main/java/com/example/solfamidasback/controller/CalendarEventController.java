package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.*;
import com.example.solfamidasback.model.DTO.CalendarEventDTO;
import com.example.solfamidasback.model.DTO.CalendarEventDTODelete;
import com.example.solfamidasback.model.DTO.CalendarEventUpdateDTO;
import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.ExternalMusicianRepository;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.CalendarEventService;
import com.example.solfamidasback.service.ExternalMusicianService;
import com.example.solfamidasback.service.JwtService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.api.client.auth.oauth2.BearerToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    ExternalMusicianRepository externalMusicianRepository;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;


    @Operation(summary = "Create a calendar event for the formation",
            description = "The calendar event is for one formation and is created by director, president or assistance controller",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("CreateEvents")
    public ResponseEntity<CalendarEvent> createCalendarEvent(@RequestBody CalendarEventDTO calendarEventDTO,
                                                             HttpServletRequest request){
        CalendarEvent calendarEvent = new CalendarEvent();

        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Token error";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        // validation that the user belongs to that formation
        if(!calendarEventService.verifyFormation(user.getFormationList(),Integer.parseInt(calendarEventDTO.getIdFormation()))){
            String mensaje = "You do not belong to that formation";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        //validation, the user must be on the formation and the rol must be owner,President, or director musical
        List<UserFormationRole> formationRoleList = user.getUserFormationRole().stream().filter(userFormationRole ->
                userFormationRole.getFormation().getId()==Integer.parseInt(calendarEventDTO.getIdFormation()))
                .collect(Collectors.toList()).stream().filter(userFormationRole
                        -> userFormationRole.getRole().getType().equals(EnumRolUserFormation.OWNER)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.ADMINISTRATOR)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.PRESIDENT)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.DIRECTOR_MUSICAL)).collect(Collectors.toList());

        if (formationRoleList.isEmpty()){
            String mensaje = "You cannot create events";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        //Validation of DTO
        if(calendarEventService.VerifyCalendarEventDTO(calendarEventDTO)) {

            //validation the date must be later than the current date
            if(LocalDate.parse(calendarEventDTO.getDate()).isBefore(LocalDate.now())||
                    LocalDate.parse(calendarEventDTO.getDate()).isEqual(LocalDate.now())){
                String mensaje = "No earlier date than the current date is possible";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
            }

            //insert calendar event
            boolean pagado = false;
            if (calendarEventDTO.getPaid().contains("1")){ pagado = true ;}

            var formation = formationRepository.findById(Integer.valueOf(calendarEventDTO.getIdFormation()));
            calendarEvent.setType(calendarEventDTO.getEnumTypeActuation().toString());
            calendarEvent.setDate(LocalDate.parse(calendarEventDTO.getDate()));
            calendarEvent.setAmount(Double.parseDouble(calendarEventDTO.getAmount()));
            calendarEvent.setDescription(calendarEventDTO.getDescription());
            calendarEvent.setPaid(pagado);
            calendarEvent.setPlace(calendarEventDTO.getPlace());
            calendarEvent.setTitle(calendarEventDTO.getTitle());
            calendarEvent.setFormation(formation.get());
            calendarEvent.setPenaltyPonderation(Double.parseDouble(calendarEventDTO.getPenaltyPonderation()));

            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }else{
            String mensaje = "Form error";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }
    }
    @Operation(summary = "Retrieve a list of calendar events for the user",
            description = "The response is a list of Calendar Event Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("AllMyEvents")
    public ResponseEntity<List<CalendarEvent>> listAllMyEvents(HttpServletRequest request){

        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Token error";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        //filter the list of caledar event by list of user formation
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll().stream().filter(calendarEvent ->
                user.getFormationList().contains(calendarEvent.getFormation())).collect(Collectors.toList());
        if(calendarEventList.isEmpty()){
            String mensaje = "ou don't have any events";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        return ResponseEntity.ok(calendarEventList);
    }

    @Operation(summary = "Retrieve a list of Calendar Event by formation",
            description = "The response is a list of Calendar Event Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("MyEventsByFormation/{idFormation}")
    public ResponseEntity<List<CalendarEvent>> listEventsByFormation(@PathVariable Integer formationId,HttpServletRequest request){


        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Token error";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        //filter the list of caledar event by list of user by formation
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll().stream().filter(calendarEvent ->
                calendarEvent.getFormation().getId().equals(formationId)).collect(Collectors.toList());
        if(calendarEventList.isEmpty()){
            String mensaje = "You don't have any events";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        return ResponseEntity.ok(calendarEventList);
    }

    @Operation(summary = "Delete a Calendar Event Objet by Calendar event id",
            description = "Delete a Calendar Event Objet by Calendar event id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteFormation(@RequestBody CalendarEventDTODelete calendarEventDTO, HttpServletRequest request) {
        if (!calendarEventService.verifyInteger(calendarEventDTO.getIdCalendarEvent())){
            String mensaje = "Incorrect format";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }
//        token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Token error";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        CalendarEvent calendarEvent = calendarEventRepository.getReferenceById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));

        //validar los roles dentro de la formacion
        //validation, the user must be on the formation and the rol must be owner,President, or director musical
        List<UserFormationRole> formationRoleList = user.getUserFormationRole().stream().filter(userFormationRole ->
                        userFormationRole.getFormation().getId()==calendarEvent.getFormation().getId())
                .collect(Collectors.toList()).stream().filter(userFormationRole
                        -> userFormationRole.getRole().getType().equals(EnumRolUserFormation.OWNER)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.ADMINISTRATOR)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.PRESIDENT)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.DIRECTOR_MUSICAL)).collect(Collectors.toList());

        if (formationRoleList.isEmpty()){
            String mensaje = "You cannot delete events";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        //validacion si ha sucedido el evento
        if(calendarEventRepository.getById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent())).getDate().isBefore(LocalDate.now())||
                calendarEventRepository.getById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent())).getDate().equals(LocalDate.now())){
            String mensaje = "El evento ya ha sucedido, no es posible eliminarlo";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        //borrado de musicos externos si tiene
        List<ExternalMusician> externalMusicianList = externalMusicianRepository.findAllByCalendarId(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));
        for (ExternalMusician m :externalMusicianList){
            externalMusicianRepository.delete(m);
        }

        calendarEventRepository.deleteById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));
        return ResponseEntity.ok("Event deleted");
    }

    @Operation(summary = "Update a Calendar Event Objet by Calendar event id",
            description = "Update a Calendar Event Objet by Calendar event id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PutMapping("update")
        public ResponseEntity<CalendarEvent> updateCalendarEvent(@RequestBody CalendarEventUpdateDTO cEUpdateDTO, HttpServletRequest request){

    //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Token error";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        //verify data of dto
        if(!calendarEventService.VerifyCalendarEventUpdateDTO(cEUpdateDTO)||
                !calendarEventService.verifyInteger(cEUpdateDTO.getIdCalendarEvent())) {
            String mensaje = "Form error";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }
        //get calendar event objet
        CalendarEvent calendarEvent = calendarEventRepository.getReferenceById(Integer.parseInt(cEUpdateDTO.getIdCalendarEvent()));

        //validation, the user must be on the formation and the rol must be owner,President, or director musical
        List<UserFormationRole> formationRoleList = user.getUserFormationRole().stream().filter(userFormationRole ->
                        userFormationRole.getFormation().getId()==calendarEvent.getFormation().getId())
                .collect(Collectors.toList()).stream().filter(userFormationRole
                        -> userFormationRole.getRole().getType().equals(EnumRolUserFormation.OWNER)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.ADMINISTRATOR)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.PRESIDENT)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.DIRECTOR_MUSICAL)).collect(Collectors.toList());

        if (formationRoleList.isEmpty()){
            String mensaje = "You cannot update events";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        //validation the date must be later than the current date
        if(LocalDate.parse(cEUpdateDTO.getDate()).isBefore(LocalDate.now())||
                LocalDate.parse(cEUpdateDTO.getDate()).isEqual(LocalDate.now())) {
            String mensaje = "No earlier date than the current date is possible";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
        }

        //si ha sucedido o es hoy solo se puede cambiar el campo pagado a true, y la descripcion
        if (calendarEvent.getDate().isBefore(LocalDate.now())||calendarEvent.getDate().equals(LocalDate.now())){
            if (cEUpdateDTO.getPaid().contains("1")){
                calendarEvent.setPaid(true);
            }
            calendarEvent.setDescription(cEUpdateDTO.getDescription());
            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }

        //si no ha sucedido
        if (calendarEvent.getDate().isAfter(LocalDate.now())){
            boolean pagado = false;
            if (cEUpdateDTO.getPaid().contains("1")){ pagado = true ;}
            calendarEvent.setType(cEUpdateDTO.getEnumTypeActuation().toString());
            calendarEvent.setDate(LocalDate.parse(cEUpdateDTO.getDate()));
            calendarEvent.setAmount(Double.parseDouble(cEUpdateDTO.getAmount()));
            calendarEvent.setDescription(cEUpdateDTO.getDescription());
            calendarEvent.setPaid(pagado);
            calendarEvent.setPlace(cEUpdateDTO.getPlace());
            calendarEvent.setTitle(cEUpdateDTO.getTitle());
            calendarEvent.setPenaltyPonderation(Double.parseDouble(cEUpdateDTO.getPenaltyPonderation()));
            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }


        String mensaje = "Something wron";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
    }

}
