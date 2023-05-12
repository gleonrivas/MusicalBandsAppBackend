package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.InvitationLinkDTO;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.service.InvitationLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "InvitationLink", description = "Invitation Link crud")
@RestController
@RequestMapping("/InvitationLink")
public class InvitationLinkController {

    @Autowired
    InvitationLinkService invitationLinkService;
    @Operation(summary = "create a invitation link in a Formation",
            description = "create a invitation link in a Formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = InvitationLinkDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/create/{idFormation}")
    public ResponseEntity<InvitationLinkDTO> createLink(@NotNull @PathVariable Integer idFormation) {

        InvitationLinkDTO invitationLinkDTO = invitationLinkService.createNewInvitation(idFormation);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(invitationLinkDTO,headers, HttpStatus.OK);
    }

    @Operation(summary = "create a invitation link in a Formation",
            description = "create a invitation link in a Formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = InvitationLinkDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PatchMapping("/{idFormation}")
    public ResponseEntity<InvitationLinkDTO> deleteLink(@NotNull @PathVariable Integer idFormation) {

        String response = invitationLinkService.deleteInvitation(idFormation);
        InvitationLinkDTO invitationLinkDTO = new InvitationLinkDTO(response);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(invitationLinkDTO,headers, HttpStatus.OK);
    }



}
