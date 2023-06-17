package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.*;
import com.example.solfamidasback.model.DTO.MusicSheetDTO;
import com.example.solfamidasback.model.DTO.UserConverter;
import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.model.converter.MusicSheetConverter;
import com.example.solfamidasback.repository.*;
import com.example.solfamidasback.service.AuthenticationService;
import com.example.solfamidasback.service.JwtService;
import com.example.solfamidasback.service.MusicSheetService;
import com.example.solfamidasback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/ms")
public class MusicSheetController {



    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FormationRepository formationRepository;
    @Autowired
    MusicSheetRepository musicSheetRepository;

    @Autowired
    UserFormationRoleRepository userFormationRoleRepository;
    @Autowired
    UserConverter userConverter;

    @Autowired
    MusicSheetConverter musicSheetConverter;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MusicSheetService musicSheetService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationService authenticateService;


    @Operation(summary = "See the musicSheet by formation",
            description = "See the musicSheet See the musicSheet by formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/listMs/{id_formation}/{id_user}")
    public ResponseEntity<List<MusicSheetDTO>> profile(HttpServletRequest request,@PathVariable Integer id_formation, @PathVariable Integer id_user) throws IOException {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MusicSheet> musicSheet = musicSheetRepository.getMusicSheetByidFormationAndUser(id_formation,id_user);

        return new ResponseEntity(musicSheetConverter.toDTO(musicSheet),headers, HttpStatus.OK);
    }

    @Operation(summary = "Save the musicSheet",
            description = "Save the musicSheet",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/create")
    public ResponseEntity<List<MusicSheet>> profile(HttpServletRequest request,  @RequestBody MusicSheetDTO msDTO) throws IOException {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);

        HttpHeaders headers = new HttpHeaders();

        Role role = new Role(true, EnumRolUserFormation.ARCHIVIST);
        roleRepository.save(role);
        role = roleRepository.findFirstOrderByIdDesc();
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(msDTO.getFormationId());
        //crear relacion
        UserFormationRole userFormationRole= new UserFormationRole(userRepository.findByIdAndActiveIsTrue(msDTO.getUserId()) ,formation,role,true);
        UserFormationRole userFR = userFormationRoleRepository.save(userFormationRole);

        MusicSheet ms = new MusicSheet();
        ms.setUserFormationRole(userFR);
        ms.setInstrumentType(1);
        ms.setMusicSheetPdf(msDTO.getMusicSheetPdf());
        musicSheetRepository.save(ms);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(ms,headers, HttpStatus.OK);
    }

}
