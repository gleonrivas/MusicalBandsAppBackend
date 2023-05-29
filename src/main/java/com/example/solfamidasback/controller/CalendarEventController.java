package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.FormationIdDTO;
import com.example.solfamidasback.controller.DTO.ResponseStringDTO;
import com.example.solfamidasback.model.*;
import com.example.solfamidasback.model.DTO.CalendarEventDTO;
import com.example.solfamidasback.model.DTO.CalendarEventDTODelete;
import com.example.solfamidasback.model.DTO.CalendarEventUpdateDTO;
import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.repository.*;
import com.example.solfamidasback.service.CalendarEventService;
import com.example.solfamidasback.service.ExternalMusicianService;
import com.example.solfamidasback.service.JwtService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.api.client.auth.oauth2.BearerToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import java.time.LocalDateTime;
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

    @Autowired
    RepertoryRepository repertoryRepository;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;


    @Operation(summary = "Create a calendar event for the formation",
            description = "The calendar event is for one formation and is created by director, president or assistance controller",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema( implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("CreateEvents")
    public ResponseEntity<CalendarEvent> createCalendarEvent(@RequestBody CalendarEventDTO calendarEventDTO,
                                                             HttpServletRequest request){
        CalendarEvent calendarEvent = new CalendarEvent();

        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Token error");
            return new ResponseEntity(responseStringDTO  , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        Users usersadmin = userRepository.findByEmailAndActiveTrueAndSuperadminTrue(mail);

        //creacion por el super admin
        if(calendarEventService.VerifyCalendarEventDTO(calendarEventDTO)&&user.equals(usersadmin)) {
            boolean pagado = false;
            if (calendarEventDTO.getPaid().contains("1")){ pagado = true ;}

            var formation = formationRepository.findById(Integer.valueOf(calendarEventDTO.getIdFormation()));
            calendarEvent.setType(calendarEventDTO.getEnumTypeActuation().toString());
            calendarEvent.setDate(LocalDateTime.parse(calendarEventDTO.getDate()));
            calendarEvent.setAmount(Double.parseDouble(calendarEventDTO.getAmount()));
            calendarEvent.setDescription(calendarEventDTO.getDescription());
            calendarEvent.setPaid(pagado);
            calendarEvent.setPlace(calendarEventDTO.getPlace());
            calendarEvent.setTitle(calendarEventDTO.getTitle());
            calendarEvent.setFormation(formation.get());
            calendarEvent.setPenaltyPonderation(Double.parseDouble(calendarEventDTO.getPenaltyPonderation()));
            calendarEvent.setRepertory(repertoryRepository.getReferenceById(Integer.parseInt(calendarEventDTO.getIdRepertory())));
            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        } else if (!calendarEventService.VerifyCalendarEventDTO(calendarEventDTO)&&user.equals(usersadmin)) {
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Form error");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        //Validation of DTO
        if(calendarEventService.VerifyCalendarEventDTO(calendarEventDTO)) {

            //validation the date must be later than the current date
            if (LocalDate.parse(calendarEventDTO.getDate()).isBefore(LocalDate.now()) ||
                    LocalDate.parse(calendarEventDTO.getDate()).isEqual(LocalDate.now())) {
                ResponseStringDTO responseStringDTO = new ResponseStringDTO("No earlier date than the current date is possible");
                return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
            }


        //validation, the user must be on the formation and the rol must be owner,President, or director musical
        List<UserFormationRole> formationRoleList = user.getUserFormationRole().stream().filter(userFormationRole ->
                userFormationRole.getFormation().getId()==Integer.parseInt(calendarEventDTO.getIdFormation()))
                .toList().stream().filter(userFormationRole
                        -> userFormationRole.getRole().getType().equals(EnumRolUserFormation.OWNER)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.ADMINISTRATOR)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.PRESIDENT)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.DIRECTOR_MUSICAL)).toList();

        if (formationRoleList.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You cannot create events");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

            //insert calendar event
            boolean pagado = false;
            if (calendarEventDTO.getPaid().contains("1")){ pagado = true ;}

            var formation = formationRepository.findById(Integer.valueOf(calendarEventDTO.getIdFormation()));
            calendarEvent.setType(calendarEventDTO.getEnumTypeActuation().toString());
            calendarEvent.setDate(LocalDateTime.parse(calendarEventDTO.getDate()));
            calendarEvent.setAmount(Double.parseDouble(calendarEventDTO.getAmount()));
            calendarEvent.setDescription(calendarEventDTO.getDescription());
            calendarEvent.setPaid(pagado);
            calendarEvent.setPlace(calendarEventDTO.getPlace());
            calendarEvent.setTitle(calendarEventDTO.getTitle());
            calendarEvent.setFormation(formation.get());
            calendarEvent.setPenaltyPonderation(Double.parseDouble(calendarEventDTO.getPenaltyPonderation()));
            calendarEvent.setRepertory(repertoryRepository.getReferenceById(Integer.parseInt(calendarEventDTO.getIdRepertory())));
            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }else{
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Form error");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }
    }

    @Operation(summary = "Retrieve a list of calendar events for the user",
            description = "The response is a list of Calendar Event Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CalendarEvent.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("AllMyEvents")
    public ResponseEntity<List<CalendarEvent>> listAllMyEvents(HttpServletRequest request){

        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Token error");
            return new ResponseEntity(responseStringDTO  , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        Users useradmin = userRepository.findByEmailAndActiveTrueAndSuperadminTrue(mail);
        //retorno de todos los eventos para el superusuario
        if (user.equals(useradmin)){
            return ResponseEntity.ok(calendarEventRepository.findAll());
        }

        //filter the list of caledar event by list of user formation
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll().stream().filter(calendarEvent ->
                user.getFormationList().contains(calendarEvent.getFormation())).collect(Collectors.toList());
        if(calendarEventList.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You don't have any events");
            return new ResponseEntity(responseStringDTO  , HttpStatus.BAD_REQUEST );
        }

        return ResponseEntity.ok(calendarEventList);
    }

    @Operation(summary = "Retrieve a list of Calendar Event by formation",
            description = "The response is a list of Calendar Event Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CalendarEvent.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("MyEventsByFormation")
    public ResponseEntity<List<CalendarEvent>> listEventsByFormation(@RequestBody FormationIdDTO formationId, HttpServletRequest request){


        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Token error");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }
        if(!calendarEventService.verifyInteger(formationId.getFormationId())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Error formationId");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        //filter the list of caledar event by list of user by formation
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll().stream().filter(calendarEvent ->
                user.getFormationList().contains(calendarEvent.getFormation())).collect(Collectors.toList()).stream()
                .filter(calendarEvent -> calendarEvent.getFormation().getId().equals(Integer.parseInt(formationId.getFormationId()))).collect(Collectors.toList());

        if(calendarEventList.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You don't have any events");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        return ResponseEntity.ok(calendarEventList);
    }

    @Operation(summary = "Delete a Calendar Event Objet by Calendar event id",
            description = "Delete a Calendar Event Objet by Calendar event id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteFormation(@RequestBody CalendarEventDTODelete calendarEventDTO, HttpServletRequest request) {
        if (!calendarEventService.verifyInteger(calendarEventDTO.getIdCalendarEvent())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Incorrect format");
            return new ResponseEntity(responseStringDTO  , HttpStatus.BAD_REQUEST );
        }
//        token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Token error");
            return new ResponseEntity(responseStringDTO  , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        Users useradmin = userRepository.findByEmailAndActiveTrueAndSuperadminTrue(mail);

        CalendarEvent calendarEvent = calendarEventRepository.getReferenceById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));

        if(user.equals(useradmin)){
            List<ExternalMusician> externalMusicianList = externalMusicianRepository.findAllByCalendarId(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));
            for (ExternalMusician m :externalMusicianList){
                externalMusicianRepository.delete(m);
            }
            calendarEventRepository.deleteById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));
            return new ResponseEntity(new ResponseStringDTO("Event deleted"),HttpStatus.OK);
        }

        //validar los roles dentro de la formacion
        //validation, the user must be on the formation and the rol must be owner,President, or director musical
        List<UserFormationRole> formationRoleList = user.getUserFormationRole().stream().filter(userFormationRole ->
                        userFormationRole.getFormation().getId()==calendarEvent.getFormation().getId())
                        .toList().stream().filter(userFormationRole
                        -> userFormationRole.getRole().getType().equals(EnumRolUserFormation.OWNER)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.ADMINISTRATOR)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.PRESIDENT)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.DIRECTOR_MUSICAL)).toList();

        if (formationRoleList.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You cannot delete events");
            return new ResponseEntity(responseStringDTO  , HttpStatus.BAD_REQUEST );
        }

        //validacion si ha sucedido el evento
        if(calendarEventRepository.getById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent())).getDate().isBefore(LocalDateTime.now())||
                calendarEventRepository.getById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent())).getDate().equals(LocalDateTime.now())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("The event has already occurred, it is not possible to delete it.");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        //borrado de musicos externos si tiene
        List<ExternalMusician> externalMusicianList = externalMusicianRepository.findAllByCalendarId(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));
        for (ExternalMusician m :externalMusicianList){
            externalMusicianRepository.delete(m);
        }

        calendarEventRepository.deleteById(Integer.parseInt(calendarEventDTO.getIdCalendarEvent()));
        return new ResponseEntity(new ResponseStringDTO("Event deleted"),HttpStatus.OK);
    }

    @Operation(summary = "Update a Calendar Event Objet by Calendar event id",
            description = "Update a Calendar Event Objet by Calendar event id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = CalendarEvent.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PutMapping("update")
        public ResponseEntity<CalendarEvent> updateCalendarEvent(@RequestBody CalendarEventUpdateDTO cEUpdateDTO, HttpServletRequest request){

    //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Token error");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        Users useradmin = userRepository.findByEmailAndActiveTrueAndSuperadminTrue(mail);

        //verify data of dto
        if(!calendarEventService.VerifyCalendarEventUpdateDTO(cEUpdateDTO)||
                !calendarEventService.verifyInteger(cEUpdateDTO.getIdCalendarEvent())) {
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Form error");
            return new ResponseEntity(responseStringDTO  , HttpStatus.BAD_REQUEST );
        }
        //get calendar event objet
        CalendarEvent calendarEvent = calendarEventRepository.getReferenceById(Integer.parseInt(cEUpdateDTO.getIdCalendarEvent()));

        //validacion del superadmin y update
        if(user.equals(useradmin)){
            boolean pagado = false;
            if (cEUpdateDTO.getPaid().contains("1")){ pagado = true ;}
            calendarEvent.setType(cEUpdateDTO.getEnumTypeActuation().toString());
            calendarEvent.setDate(LocalDateTime.parse(cEUpdateDTO.getDate()));
            calendarEvent.setAmount(Double.parseDouble(cEUpdateDTO.getAmount()));
            calendarEvent.setDescription(cEUpdateDTO.getDescription());
            calendarEvent.setPaid(pagado);
            calendarEvent.setPlace(cEUpdateDTO.getPlace());
            calendarEvent.setTitle(cEUpdateDTO.getTitle());
            calendarEvent.setPenaltyPonderation(Double.parseDouble(cEUpdateDTO.getPenaltyPonderation()));
            calendarEvent.setRepertory(repertoryRepository.getReferenceById(Integer.parseInt(cEUpdateDTO.getIdRepertory())));
            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }

        //validation, the user must be on the formation and the rol must be owner,President, or director musical
        List<UserFormationRole> formationRoleList = user.getUserFormationRole().stream().filter(userFormationRole ->
                        userFormationRole.getFormation().getId()==calendarEvent.getFormation().getId())
                        .toList().stream().filter(userFormationRole
                        -> userFormationRole.getRole().getType().equals(EnumRolUserFormation.OWNER)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.ADMINISTRATOR)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.PRESIDENT)||
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.DIRECTOR_MUSICAL)).toList();

        if (formationRoleList.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You cannot update events");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }
//        //validation the date must be later than the current date
//        if(LocalDate.parse(cEUpdateDTO.getDate()).isBefore(LocalDate.now())||
//                LocalDate.parse(cEUpdateDTO.getDate()).isEqual(LocalDate.now())) {
//            ResponseStringDTO responseStringDTO = new ResponseStringDTO("No earlier date than the current date is possible");
//            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
//        }

        //si ha sucedido o es hoy solo se puede cambiar el campo pagado a true, y la descripcion
        if (calendarEvent.getDate().isBefore(LocalDateTime.now())||calendarEvent.getDate().equals(LocalDateTime.now())){
            if (cEUpdateDTO.getPaid().contains("1")){
                calendarEvent.setPaid(true);
            }
            calendarEvent.setDescription(cEUpdateDTO.getDescription());
            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }

        //si no ha sucedido
        if (calendarEvent.getDate().isAfter(LocalDateTime.now())){
            boolean pagado = false;
            if (cEUpdateDTO.getPaid().contains("1")){ pagado = true ;}
            calendarEvent.setType(cEUpdateDTO.getEnumTypeActuation());
            calendarEvent.setDate(LocalDateTime.parse(cEUpdateDTO.getDate()));
            calendarEvent.setAmount(Double.parseDouble(cEUpdateDTO.getAmount()));
            calendarEvent.setDescription(cEUpdateDTO.getDescription());
            calendarEvent.setPaid(pagado);
            calendarEvent.setPlace(cEUpdateDTO.getPlace());
            calendarEvent.setTitle(cEUpdateDTO.getTitle());
            calendarEvent.setPenaltyPonderation(Double.parseDouble(cEUpdateDTO.getPenaltyPonderation()));
            if(cEUpdateDTO.getIdRepertory()!= ""){
                calendarEvent.setRepertory(repertoryRepository.getReferenceById(Integer.parseInt(cEUpdateDTO.getIdRepertory())));
            }
            calendarEventRepository.save(calendarEvent);
            return ResponseEntity.ok(calendarEvent);
        }

        ResponseStringDTO responseStringDTO = new ResponseStringDTO("Something wron");
        return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
    }

    @Operation(summary = "Retrieve a list of calendar events for the user",
            description = "The response is a list of Calendar Event Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CalendarEvent.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("TodayEventByFormation")
    public ResponseEntity<List<CalendarEvent>> listAllMyEvents(@RequestBody FormationIdDTO formationId, HttpServletRequest request) {

        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        } catch (Exception e) {
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Token error");
            return new ResponseEntity(responseStringDTO, HttpStatus.BAD_REQUEST);
        }

        if(!calendarEventService.verifyInteger(formationId.getFormationId())){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("Error formationId");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail = jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        //filter the list of caledar event by list of user by formation
        List<CalendarEvent> calendarEventList = calendarEventRepository.findAll().stream().filter(calendarEvent ->
                        user.getFormationList().contains(calendarEvent.getFormation())).collect(Collectors.toList()).stream()
                .filter(calendarEvent -> calendarEvent.getFormation().getId().equals(Integer.parseInt(formationId.getFormationId()))).
                collect(Collectors.toList()).stream().filter(calendarEvent -> calendarEvent.getDate().equals(LocalDate.now())).collect(Collectors.toList());

        if(calendarEventList.isEmpty()){
            ResponseStringDTO responseStringDTO = new ResponseStringDTO("You don't have any events");
            return new ResponseEntity(responseStringDTO , HttpStatus.BAD_REQUEST );
        }

        return ResponseEntity.ok(calendarEventList);
    }

    @Operation(summary = "Event by id",
            description = "find event by id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CalendarEvent.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = String.class))}),
    })
    @GetMapping("findEvent/{idEvent}")
    public ResponseEntity<CalendarEvent> createCalendarEvent(@PathVariable Integer idEvent) {

        CalendarEvent calendarEvent = calendarEventRepository.findCalendarEventById(idEvent);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(calendarEvent == null){
            return new ResponseEntity(new CalendarEvent(), headers, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity(calendarEvent, headers, HttpStatus.OK);
        }
    }

    @Operation(summary = "Formation byy calendar",
            description = "Formation byy calendar",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Formation.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = String.class))}),
    })
    @GetMapping("findFormation/{idEvent}")
    public ResponseEntity<Formation> fromationsbyCalendar(@PathVariable Integer idEvent) {

        CalendarEvent calendarEvent = calendarEventRepository.findCalendarEventById(idEvent);
        Formation formation = calendarEventService.findFormationbyCalendar(calendarEvent);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(calendarEvent == null || formation == null){
            return new ResponseEntity(new Formation(), headers, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity(formation, headers, HttpStatus.OK);
        }
    }


}
