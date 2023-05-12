package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.ResponseStringDTO;
import com.example.solfamidasback.model.DTO.RepertoryDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.MusicalPiece;
import com.example.solfamidasback.model.Repertory;
import com.example.solfamidasback.service.RepertoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Repertory", description = "Repertory crud")
@RestController
@RequestMapping("/repertory")
public class RepertoryController {
    @Autowired
    RepertoryService repertoryService;

    @Operation(summary = "Retrieve a repertory",
            description = "The response is a repertory by his id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = RepertoryDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}")
    public ResponseEntity<RepertoryDTO> repertoryById(@PathVariable Integer id) {

        RepertoryDTO repertoryDTO = repertoryService.findUniqueById(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(repertoryDTO, httpHeaders, HttpStatus.OK);

    }



    @Operation(summary = "Retrieve a list of repertories",
            description = "The response is a list of repertory",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema( schema = @Schema(implementation = RepertoryDTO.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),

    })
    @GetMapping("")
    public ResponseEntity<List<RepertoryDTO>> listRepertory() {

        List<RepertoryDTO> repertoryDTOList = repertoryService.findById();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(repertoryDTOList,httpHeaders, HttpStatus.OK);
    }


    @Operation(summary = "Retrieve a list of repertory",
            description = "The response is a list of repertory by id Formation",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(array = @ArraySchema( schema = @Schema(implementation = RepertoryDTO.class)),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),

    })
    @GetMapping("list/{idFormation}")
    public ResponseEntity<List<RepertoryDTO>> listRepertoryByIdFormation(@PathVariable Integer idFormation) {

        List<RepertoryDTO> repertoryDTOList = repertoryService.findByIdFormation(idFormation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(repertoryDTOList,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Creatre a repertory",
            description = "Creatre a repertory",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Repertory.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/create")
    public ResponseEntity<Repertory> createRepertory(@RequestBody RepertoryDTO repertoryDTO) {
        Repertory repertory = repertoryService.createUpdate(repertoryDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(repertory,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Update a repertory",
            description = "modify a repertory",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Repertory.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PutMapping("/update")
    public ResponseEntity<Repertory> updateRepertory(@RequestBody RepertoryDTO repertoryDTO) {
        Repertory repertory = repertoryService.createUpdate(repertoryDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(repertory,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Delete a repertory by id",
            description = "Delete a repertory piece by id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @DeleteMapping("/delete/{idRepertory}")
    public ResponseEntity<String> deleteFormation(@PathVariable Integer idRepertory) {
        repertoryService.modifyActive(idRepertory);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(new ResponseStringDTO("the repertory has been deleted"),httpHeaders, HttpStatus.OK);
    }


}
