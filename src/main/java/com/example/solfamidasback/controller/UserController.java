package com.example.solfamidasback.controller;

import com.dropbox.core.DbxException;
import com.example.solfamidasback.configSecurity.driveCredentials.GoogleDriveBasic;
import com.example.solfamidasback.configSecurity.dropbox.DropboxConfig;
import com.example.solfamidasback.controller.DTO.PasswordDTO;
import com.example.solfamidasback.controller.DTO.ResponseStringDTO;
import com.example.solfamidasback.model.DTO.InvitationLinkDTO;
import com.example.solfamidasback.model.DTO.SuperAdminDTO;
import com.example.solfamidasback.model.DTO.UserConverter;
import com.example.solfamidasback.model.DTO.UserDTO;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.AuthenticationService;
import com.example.solfamidasback.service.JwtService;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {


    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Autowired
    private JwtService jwtService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserConverter userConverter;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;



    @Autowired
    AuthenticationService authenticateService;


    @Operation(summary = "List Users",
            description = "List Users",
            security = @SecurityRequirement(name = "bearerAuth"))

    @ApiResponses({
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/list")
    public ResponseEntity<List<Users>>  listUsers(){
        List<Users> listUsers = userRepository.findAllByActiveTrue();
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        Users  users = userRepository.findByEmailAndActiveTrue(authentication);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(listUsers,headers, HttpStatus.OK);
    }


//    @PutMapping("/update")
//    public ResponseEntity<Users> updateUser(@RequestBody UserUpdateDTO userUpdateDTO){
//
//        Users user = userRepository.findByIdAndActiveIsTrue(userUpdateDTO.getId());
//
//        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        if (userUpdateDTO.getId() == null){
//            String mensaje = "Id de usuario no válido";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.TEXT_PLAIN);
//            return new ResponseEntity(mensaje, headers, HttpStatus.BAD_REQUEST);
//        }
//
//
//        user.setName(userUpdateDTO.getName());
//        user.setSurName(userUpdateDTO.getSurName());
//        user.setEmail(userUpdateDTO.getEmail());
//        user.setBirthDate(LocalDateTime.parse(userUpdateDTO.getBirthDate().replace(" ", "T"), formater));
//        user.setDni(userUpdateDTO.getDni());
//        user.setSuperadmin(userUpdateDTO.getSuperadmin());
//
//        if (userUpdateDTO.getPassword() != null) {
//            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
//        }
//
//        userRepository.save(user);
//
//
//        String mensaje = "Usuario cambiado con éxito";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        return new ResponseEntity(mensaje, headers, HttpStatus.OK);
//
//    }

    @Operation(summary = "List Users by name",
            description = "List Users by name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/list/{name}")
    public @ResponseBody List<Users> listUsersByName(@PathVariable String name) throws JsonProcessingException {

        return Collections.singletonList(userRepository.findAllByNameAndActiveIsTrue(name));

    }
    @Operation(summary = "Edit your profile",
            description = "Edit your profile",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = UserDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/editProfile")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user, HttpServletRequest request ) throws DbxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users userSession = userRepository.findByEmailAndActiveTrue(mail);
        userSession.setName(user.getName());
        userSession.setSurName(user.getSurName());
        userSession.setEmail(user.getEmail());
        userSession.setBirthDate(LocalDate.parse(user.getBirthDate()).atStartOfDay());
        userSession.setDni(user.getDni());
        userSession.setUrl(DropboxConfig.UploadFile(user.getUrl(), user.getEmail()));


        userRepository.save(userSession);
        return new ResponseEntity("user edited successfully",headers, HttpStatus.OK);

    }
    @Operation(summary = "See your profile",
            description = "See your profile",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/profile")
    public ResponseEntity<Users> profile(HttpServletRequest request) throws IOException {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO userDTO = userConverter.toDTO(user);
        return new ResponseEntity(userDTO,headers, HttpStatus.OK);
    }
    @Operation(summary = "Delete your user",
            description = "Delete your user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/deleteProfile")
    public ResponseEntity<String> deleteProfile(HttpServletRequest request){
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        user.setActive(false);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("user deleted successfully",headers, HttpStatus.OK);

    }

    @Operation(summary = "Create a SuperAdmin",
            description = "Create a SuperAdmin",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = SuperAdminDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/createAdmin")
    public ResponseEntity<String> registerAdmin(@RequestBody SuperAdminDTO user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        userRepository.save(userConverter.fromAdmintoEntity(user));
        return new ResponseEntity("admin created successfully",headers, HttpStatus.OK);

    }

    @Operation(summary = "Delete your SuperAdmin",
            description = "Delete your SuperAdmin",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/deleteAdmin")
    public ResponseEntity<String> deleteAdminProfile(HttpServletRequest request){
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrueAndSuperadminTrue(mail);
        user.setActive(false);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("admin deleted successfully",headers, HttpStatus.OK);

    }
    @Operation(summary = "Change your password",
            description = "Change your password",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = PasswordDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String mail =  jwtService.extractUsername(jwtToken);
        Users user = userRepository.findByEmailAndActiveTrue(mail);
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(authenticateService.authenticatePassword(passwordDTO.getOldPassword(),request)){
            if (passwordDTO.getNewPassword1().equals(passwordDTO.getNewPassword2())){
                user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword1()));
                userRepository.save(user);
                return new ResponseEntity(new ResponseStringDTO("password changed successfully"),headers, HttpStatus.OK);
            }else {
                return new ResponseEntity(new ResponseStringDTO("new passwords are not the same"),headers, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }else {
            return new ResponseEntity(new ResponseStringDTO("old passwords are not the same"),headers, HttpStatus.UNPROCESSABLE_ENTITY);
        }


    }




}
