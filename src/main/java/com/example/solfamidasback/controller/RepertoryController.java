package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.MusicalPieceDTO;
import com.example.solfamidasback.model.DTO.RepertoryDTO;
import com.example.solfamidasback.model.MusicalPiece;
import com.example.solfamidasback.model.Repertory;
import com.example.solfamidasback.model.converter.RepertoryConverter;
import com.example.solfamidasback.service.RepertoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Repertory", description = "Repertory crud")
@RestController
@RequestMapping("/repertory")
public class RepertoryController {
    @Autowired
    RepertoryService repertoryService;

    @Operation(summary = "Retrieve a list of repertory",
            description = "The response is a list of repertory by id Formation",
            tags = {"idFormation"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Repertory.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{idFormation}")
    public ResponseEntity<List<RepertoryDTO>> listRepertoryByIdFormation(@PathVariable Integer idFormation) {

        List<RepertoryDTO> repertoryDTOList = repertoryService.findByIdFormation(idFormation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(repertoryDTOList,httpHeaders, HttpStatus.OK);
    }
}
