package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.ExternalMusicianDTO;
import com.example.solfamidasback.controller.DTO.ExternalMusicianUpdateDTO;
import com.example.solfamidasback.model.ExternalMusician;
import com.example.solfamidasback.service.ExternalMusicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "ExternalMusician", description = "External Musician crud")
@RestController
@RequestMapping("/ExternalMusician")
public class ExternalMusicianController {
    @Autowired
    ExternalMusicianService externalMusicianService;

    @Operation(summary = "Retrieve a external musician by calendar id",
            description = "The response is a external musician Objects")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = ExternalMusician.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/findByCalendar/{calendarId}")
    public ResponseEntity<List<ExternalMusician>> externalMusicianByIdCalendar(@PathVariable Integer calendarId) {
        List<ExternalMusician> externalMusicianList = externalMusicianService.findByCalendar(calendarId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);

    }

    @Operation(summary = "Retrieve a external musician by name",
            description = "The response is a external musician Objects")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = ExternalMusician.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{name}")
    public ResponseEntity<List<ExternalMusician>> externalMusicianByName(@PathVariable String name) {
        List<ExternalMusician> externalMusicianList = externalMusicianService.findByName(name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);

    }
    @Operation(summary = "Retrieve a external musician by id",
            description = "The response is a external musician Objects")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = ExternalMusician.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/findBy/{id}")
    public ResponseEntity<ExternalMusician> externalMusicianById(@PathVariable Integer id) {
        Optional<ExternalMusician> externalMusician = externalMusicianService.findById(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusician,httpHeaders, HttpStatus.OK);

    }
    @Operation(summary = "Retrieve a external musician created",
            description = "The response is a external musician Object")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = ExternalMusician.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/create")
    public ResponseEntity<ExternalMusician> createExternalMusician(@RequestBody ExternalMusicianDTO externalMusicianDTO){
        ExternalMusician externalMusician = externalMusicianService.create(externalMusicianDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusician,httpHeaders, HttpStatus.OK);
    }
    @Operation(summary = "Retrieve a external musician updated",
            description = "The response is a external musician Object")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = ExternalMusician.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PutMapping("/update")
    public ResponseEntity<ExternalMusician> updateExternalMusician(@RequestBody ExternalMusicianUpdateDTO externalMusicianUpdateDTO){
        ExternalMusician externalMusician = externalMusicianService.update(externalMusicianUpdateDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusician,httpHeaders, HttpStatus.OK);
    }
    @Operation(summary = "Retrieve a message",
            description = "the external musician is deletec")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = ExternalMusician.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> deleteExternalMusician(@PathVariable @NotNull Integer id){
        String response = externalMusicianService.deleteById(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(response,httpHeaders, HttpStatus.OK);
    }

}
