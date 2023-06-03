package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.FormationLikeDTO;
import com.example.solfamidasback.controller.DTO.FormationUserDeleteDTO;
import com.example.solfamidasback.controller.DTO.ResponseStringDTO;
import com.example.solfamidasback.controller.DTO.UsersFormationRoleDTO;
import com.example.solfamidasback.model.*;
import com.example.solfamidasback.model.DTO.FormationDTO;
import com.example.solfamidasback.model.DTO.FormationUpdateDTO;
import com.example.solfamidasback.model.DTO.InvitationLinkDTO;
import com.example.solfamidasback.repository.*;
import com.example.solfamidasback.service.*;
import com.example.solfamidasback.utilities.Utilities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag(name = "Formation", description = "Formation crud")
@RestController
@RequestMapping("/formation")
public class FormationController {

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserFormationRoleRepository userFormationRoleRepository;

    @Autowired
    private TreasuryRepository treasuryRepository;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;


    @Autowired
    FormationService formationService;

    @Autowired
    UserFormationRoleService userFormationRoleService;


    @Operation(summary = "Retrieve a list of formation by user id",
            description = "The response is a list of Formation Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema( schema = @Schema(implementation = Formation.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/listByUser")
    public ResponseEntity<List<Formation>> listFormationByUserAndActive(HttpServletRequest request) {

        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        Set<Formation> formationSet = new HashSet<>(formationRepository.getAllByUserAndActiveIsTrue(user.getId()));
        List<Formation> formationList = new ArrayList<>(formationSet);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(formationList,httpHeaders, HttpStatus.OK);

    }
    @Operation(summary = "Retrieve a list of formation by owner",
            description = "The response is a list of Formation Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema( schema = @Schema(implementation = Formation.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/listByOwner")
    public ResponseEntity<List<Formation>> listFormationByOwnerUserAndActive(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        List<Formation> formationList = formationRepository.getAllByUserOwnerAndActiveIsTrue(user.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(formationList,headers, HttpStatus.OK);

    }
    @Operation(summary = "Retrieve a formation by id",
            description = "The response is a Formation Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/listById/{formationId}")
    public ResponseEntity<Formation> formationById(@PathVariable Integer formationId) {
        return ResponseEntity.ok(formationRepository.findFormationByIdAndActiveIsTrue(formationId));
    }

    @Operation(summary = "Create a formation",
            description = "Create formation with user_id as administrator of the formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/create")
    public ResponseEntity<Formation> createFormation(@RequestBody FormationDTO formationDTO,
                                                     HttpServletRequest request) {
        //buscar el user
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        //crear el rol de propietario de formacion
        Role role = roleService.createRoleFormationAdministrator();
        //guardar el nuevo rol
        roleRepository.save(role);
        //crear la formacion
        Formation formation = new Formation();
        formation.setActive(true);
        formation.setLogo(formationDTO.getLogo());
        formation.setName(formationDTO.getName());
        formation.setDesignation(formationDTO.getDesignation());
        formation.setType(formationDTO.getType());
        formation.setLink(Utilities.createLink());
        formation.setFundationDate(LocalDateTime.parse(formationDTO.getFundationDate()));
        formation.setUsers(user);
        formationRepository.save(formation);
        //bucar la formación
        Formation formationCreated = formationRepository.findLastFormation();
        //crear relación user_formation_role
        UserFormationRole userFormationRole = new UserFormationRole(user, formationCreated, role);
        userFormationRoleRepository.save(userFormationRole);
        //crear la tesorería
        Treasury treasury = new Treasury();
        treasury.setFormation(formation);
        treasury.setReceiveMoneyDate(LocalDate.now());
        treasury.setAmount(0.0);
        treasury.setActive(true);
        treasuryRepository.save(treasury);

        return ResponseEntity.ok(formation);
    }
    @Operation(summary = "Update a formation by id",
            description = "Uptate a formation by id ",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PutMapping("/update")
    public ResponseEntity<Formation> updateFormation(@RequestBody FormationUpdateDTO formationupdateDTO,
                                                     HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(formationupdateDTO.getId());
        formation.setActive(true);
        formation.setLogo(formationupdateDTO.getLogo());
        formation.setName(formationupdateDTO.getName());
        formation.setDesignation(formationupdateDTO.getDesignation());
        formation.setType(formationupdateDTO.getType());
        formation.setFundationDate(LocalDateTime.parse(formationupdateDTO.getFundationDate(), formatter));
        formation.setUsers(user);
        formationRepository.save(formation);

        return ResponseEntity.ok(formation);
    }

    @Operation(summary = "Delete a formation by id",
            description = "Delete a formation by id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @DeleteMapping("/delete/{idFormation}")
    public ResponseEntity<String> deleteFormation(@PathVariable Integer idFormation) {

        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        formation.setActive(false);
        formationRepository.save(formation);
        return ResponseEntity.ok("formation deleted");
    }

    @Operation(summary = "Delete a user from a formation",
            description = "Delete a user from a formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/deleteUserFormation")
    public ResponseEntity<ResponseEntity> deleteUserFormation(@NotNull @RequestBody FormationUserDeleteDTO formationUserDeleteDTO){
        String result = formationService.deleteUserFormation(formationUserDeleteDTO.getFormationId(),formationUserDeleteDTO.getUserId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Insert a user in a formation",
            description = "Insert a user in a formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/reactiveUserFormation")
    public ResponseEntity<String> reactiveUserFormation(@NotNull @RequestBody FormationUserDeleteDTO formationUserDeleteDTO){
        String result = formationService.reactiveUserFormation(formationUserDeleteDTO.getFormationId(),formationUserDeleteDTO.getUserId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(result,headers, HttpStatus.OK);

    }
    @Operation(summary = "Search a formation by name",
            description = "Search a formation by name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema( schema = @Schema(implementation = Formation.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/searchByName")
    public ResponseEntity<List<Formation>> searchByNameLike(@NotNull @RequestBody FormationLikeDTO formationLikeDTO){
        List<Formation> formations = formationRepository.findFormationsByLike(formationLikeDTO.getNameFormation());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(formations,headers, HttpStatus.OK);

    }

    @Operation(summary = "Insert an user in a formation by a invitation link",
            description = "Insert an user in a formation by a invitation link",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/addUser")
    public ResponseEntity<ResponseStringDTO> addingByInvitationLink(@NotNull @RequestBody InvitationLinkDTO invitationLinkDTO,
                                                                    HttpServletRequest request){
        ResponseStringDTO responseDTO = new ResponseStringDTO();
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        String response = formationService.addingByInvitationLink(invitationLinkDTO, user);
        responseDTO.setResponse(response);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(responseDTO,headers, HttpStatus.OK);

    }


    @Operation(summary = "List the users of a formation",
            description = "List the users of a formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema( schema = @Schema(implementation = UsersFormationRoleDTO.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/listUsers/{idFormation}")
    public ResponseEntity<List<UsersFormationRoleDTO>> usersByAFormation(@NotNull @PathVariable Integer idFormation){
        List<UsersFormationRoleDTO> usersList = formationService.listUsersByFormation(idFormation);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(usersList,headers, HttpStatus.OK);

    }


    @Operation(summary = "Retrieve a formation by invitation link",
            description = "The response is a Formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = FormationDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/findByInvitationLink")
    public ResponseEntity<Formation> formationByInvitationLink(@NotNull @RequestBody  InvitationLinkDTO invitationLinkDTO) {
        Formation formation = formationRepository.findByLinkAndActiveIsTrue(invitationLinkDTO.getLink());
        return ResponseEntity.ok(formation);
    }


}
