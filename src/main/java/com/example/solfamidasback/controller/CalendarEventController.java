package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.CalendarEventDTO;
import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.UserRepository;
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
        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Error de token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);

        // validation that the user belongs to that formation
        if(!calendarEventService.verifyFormation(user.getFormationList(),Integer.parseInt(calendarEventDTO.getIdFormation()))){
            String mensaje = "No perteneces a esa formacion";
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
                        userFormationRole.getRole().getType().equals(EnumRolUserFormation.PRESIDENT)).collect(Collectors.toList());

        if (formationRoleList.isEmpty()){
            String mensaje = "no puedes crear eventos";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        //Validation of DTO
        CalendarEvent calendarEvent = new CalendarEvent();
        if(calendarEventService.VerifyCalendarEventDTO(calendarEventDTO)) {

            //validation the date must be later than the current date
            if(LocalDate.parse(calendarEventDTO.getDate()).isBefore(LocalDate.now())||
                    LocalDate.parse(calendarEventDTO.getDate()).isEqual(LocalDate.now())){
                String mensaje = "No es posible una fecha anterior a la actual";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.TEXT_PLAIN);
                return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
            }

            //insert calendar event
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

            calendarEventRepository.save(calendarEvent);
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
    @GetMapping("AllMyEvents")
    public ResponseEntity<List<CalendarEvent>> listAllMyEvents(HttpServletRequest request){

        //filtrar por el token
        //token validation
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Error de token";
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
            String mensaje = "No tienes eventos";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        return ResponseEntity.ok(calendarEventList);
    }
    //Método para ver eventos por formacion
    @GetMapping("MyEventsByFormation")
    public ResponseEntity<List<CalendarEvent>> listEventsByFormation(@RequestParam Integer formationId,HttpServletRequest request){

        //filtrar por el token
        //token validation
        System.out.println("ID FORMATION "+formationId);
        try {
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        }catch (Exception e){
            String mensaje = "Error de token";
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
            String mensaje = "No tienes eventos";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity(mensaje ,headers , HttpStatus.BAD_REQUEST );
        }

        return ResponseEntity.ok(calendarEventList);
    }


    //para el update, dos tipos, si aun no ha sucedido el evento, se puede eliminar , y modificar los campos,
    //si ha sucedido, solo se puede modificar la descripcion y si no está pagado a pagado
}
