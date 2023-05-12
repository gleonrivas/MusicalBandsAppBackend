package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.RoleDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.RoleRepository;
import com.example.solfamidasback.repository.UserFormationRoleRepository;
import com.example.solfamidasback.repository.UserRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag(name = "Role", description = "Role crud")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleRepository roleRepository;
    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserFormationRoleRepository userFormationRoleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private FormationRepository formationRepository;

    @Operation(summary = "Retrieve a list of roles",
            description = "The response is a list of Role Objects",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema( schema = @Schema(implementation = Role.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),

    })
    @GetMapping("/list/{idFormation}")
    public ResponseEntity<List<Role>> list(@PathVariable Integer idFormation,

                                                             HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        //traer lista idrol
        Set<Integer> idRoleSet = new HashSet<>(userFormationRoleRepository.idRolByFormationAndUser(idFormation, user.getId()));
        List<Integer> idRoleList = new ArrayList<>(idRoleSet);
        //traer lista rol
        List<Role> roleList = new ArrayList<>();
        for(Integer idRole: idRoleList){
            Role role = roleRepository.findByActiveIsTrueAndId(idRole);
            if(role != null){
                roleList.add(role);
            }
        }
        return ResponseEntity.ok(roleList);
    }

    @Operation(summary = "Create a role in a formation",
            description = "Create role of an user in a formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Role.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),

    })
    @PostMapping("/create/{idFormation}")
    public ResponseEntity<Role> createRole(@RequestBody RoleDTO roleDTO, HttpServletRequest request,
                                           @PathVariable Integer idFormation) {
        //buscar el user
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        //crear rol
        Role role = new Role(true,roleDTO.getType());
        roleRepository.save(role);
        role = roleRepository.findFirstOrderByIdDesc();
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        //crear relacion
        UserFormationRole userFormationRole= new UserFormationRole(user,formation,role);
        userFormationRoleRepository.save(userFormationRole);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(role,headers, HttpStatus.OK);
    }


    @Operation(summary = "Change the status of a role to false",
            description = "Change an user rol from a formation to active is false",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @PutMapping("/delete/{idRole}")
    public ResponseEntity<String> rolActiveFalse(@PathVariable Integer idRole) {
        Role role = roleRepository.findByActiveIsTrueAndId(idRole);
        role.setActive(false);
        roleRepository.save(role);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("role deleted",headers, HttpStatus.OK);
    }



}
