package com.example.solfamidasback.controller;

import com.example.solfamidasback.configSecurity.AuthenticationResponses;
import com.example.solfamidasback.controller.DTO.FormationDTO;
import com.example.solfamidasback.controller.DTO.FormationUpdateDTO;
import com.example.solfamidasback.model.Enums.EnumFormationType;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.RoleRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.RoleService;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    @Operation(summary = "Retrieve a list of formation by user id",
                description = "The response is a list of Formation Objects",
                tags = {"user_id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/listByUser/{user_id}")
    public ResponseEntity<List<Formation>> listFormationByUserAndActive(@PathVariable Integer user_id) {
        List<Formation> formationList = formationRepository.getAllByUserAndActiveIsTrue(user_id);
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
    @GetMapping("/listById/{formation_id}")
    public ResponseEntity<Formation> formationById(@PathVariable Integer formation_id) {
        return ResponseEntity.ok(formationRepository.findFormationByIdAndActiveIsTrue(formation_id));
    }


    @Operation(summary = "Create a formation",
            description = "Create formation with user_id as administrator of the formation",
            tags = {"user_id","name","designation","type","fundation date","logo"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/create")
    public ResponseEntity<Formation> createFormation(@RequestBody FormationDTO formationDTO) {
        //buscar el user
        Users user = userRepository.findByIdAndActiveIsTrue(formationDTO.getId_user());
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
        return ResponseEntity.ok(formation);
    }
    @Operation(summary = "Update a formation by id",
            description = "Uptate a formation by id ",
            tags = {"id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/update")
    public ResponseEntity<Formation> updateFormation(@RequestBody FormationUpdateDTO formationupdateDTO) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(formationupdateDTO.getId());
        Users user = userRepository.findByIdAndActiveIsTrue(formationupdateDTO.getId_user());
        formation.setActive(true);
        formation.setLogo(formationupdateDTO.getLogo());
        formation.setName(formationupdateDTO.getName());
        formation.setDesignation(formationupdateDTO.getDesignation());
        formation.setType(formationupdateDTO.getType());
        formation.setFundationDate(LocalDateTime.parse(formationupdateDTO.getFundationDate().replace(" ", "T"), formatter));
        formation.setUsers(user);

        formationRepository.save(formation);
        //bucar la formación para coger el id

        return ResponseEntity.ok(formation);
    }

    @Operation(summary = "Delete a formation by id",
            description = "Delete a formation by id",
            tags = {"id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @DeleteMapping("/delete/{id_formation}")
    public ResponseEntity<String> deleteFormation(@PathVariable Integer id_formation) {

        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(id_formation);
        formation.setActive(false);
        formationRepository.save(formation);
        return ResponseEntity.ok("formation deleted");
    }

}
