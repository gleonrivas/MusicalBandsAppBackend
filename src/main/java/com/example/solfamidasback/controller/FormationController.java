package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.FormationDTO;
import com.example.solfamidasback.model.DTO.FormationUpdateDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.RoleRepository;
import com.example.solfamidasback.repository.UserFormationRoleRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.JwtService;
import com.example.solfamidasback.service.RoleService;
import com.example.solfamidasback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;



    @Operation(summary = "Retrieve a list of formation by user id",
            description = "The response is a list of Formation Objects",
            tags = {"user_id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
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
            tags = {"formation_id"})
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
            tags = {"user_id","name","designation","type","fundation date","logo"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
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
        formation.setFundationDate(LocalDateTime.parse(formationDTO.getFundationDate()));
        formation.setUsers(user);
        formationRepository.save(formation);
        //bucar la formación
        Formation formationCreated = formationRepository.findLastFormation();
        //crear relación user_formation_role
        UserFormationRole userFormationRole = new UserFormationRole(user, formationCreated, role);
        userFormationRoleRepository.save(userFormationRole);

        return ResponseEntity.ok(formation);
    }
    @Operation(summary = "Update a formation by id",
            description = "Uptate a formation by id ",
            tags = {"id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
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
            tags = {"id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @DeleteMapping("/delete/{idFormation}")
    public ResponseEntity<String> deleteFormation(@PathVariable Integer idFormation) {

        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        formation.setActive(false);
        formationRepository.save(formation);
        return ResponseEntity.ok("formation deleted");
    }

}
