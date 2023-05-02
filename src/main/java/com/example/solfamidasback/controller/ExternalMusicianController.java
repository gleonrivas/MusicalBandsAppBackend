package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.ExternalMusician;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.service.ExternalMusicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

@Tag(name = "ExternalMusician", description = "External Musician crud")
@RestController
@RequestMapping("/ExternalMusician")
public class ExternalMusicianController {
    @Autowired
    ExternalMusicianService externalMusicianService;

    @Operation(summary = "Retrieve a external musician by calendar id",
            description = "The response is a external musician Objects",
            tags = {"calendar_id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{calendarId}")
    public ResponseEntity<List<ExternalMusician>> externalMusicianById(@PathVariable Integer calendarId) {
        List<ExternalMusician> externalMusicianList = externalMusicianService.findByCalendar(calendarId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);

    }

    @Operation(summary = "Retrieve a external musician by id",
            description = "The response is a external musician Objects",
            tags = {"calendar_id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Formation.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{name}")
    public ResponseEntity<List<ExternalMusician>> externalMusicianByName(@PathVariable String name) {
        List<ExternalMusician> externalMusicianList = externalMusicianService.findByName(name);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);

    }

}
