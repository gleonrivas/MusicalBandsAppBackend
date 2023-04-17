package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.DTO.MusicalPieceDTO;
import com.example.solfamidasback.model.MusicalPiece;
import com.example.solfamidasback.repository.MusicalPieceRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag(name = "MusicalPiece", description = "Musical piece crud")
@RestController
@RequestMapping("/musicalPiece")
public class MusicalPieceController {
    @Autowired
    MusicalPieceRepository musicalPieceRepository;

    @Operation(summary = "Retrieve a list of musical Piece",
            description = "The response is a list of Musical Piece")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MusicalPiece.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("")
    public ResponseEntity<List<MusicalPieceDTO>> listMusicalPieceActive(HttpServletRequest request) {

        List<MusicalPiece> musicalPieceList = musicalPieceRepository.findAllByActiveIsTrue();
        List<MusicalPieceDTO> musicalPieceDTOList = new ArrayList<>();
        for(MusicalPiece musicalPiece: musicalPieceList){
            MusicalPieceDTO musicalPieceDTO = new MusicalPieceDTO(musicalPiece.getName(),musicalPiece.getAuthor(), musicalPiece.getLength());
            musicalPieceDTOList.add(musicalPieceDTO);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPieceDTOList,httpHeaders, HttpStatus.OK);
    }
    @Operation(summary = "Retrieve a list of musical Piece",
            description = "The response is a list of Musical Piece",
            tags = {"idRepertory"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MusicalPiece.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{idRepertory}")
    public ResponseEntity<List<MusicalPieceDTO>> listMusicalPieceActiveByRepertoire(@PathVariable Long idRepertory) {

        Set<MusicalPiece> musicalPieceSet = new HashSet<>(musicalPieceRepository.musicalPieceByIdRepertoireAndActiveTrue(idRepertory));
        List<MusicalPiece> musicalPieceList = new ArrayList<>(musicalPieceSet);
        List<MusicalPieceDTO> musicalPieceDTOList = new ArrayList<>();
        for(MusicalPiece musicalPiece: musicalPieceList){
            musicalPieceDTOList.add(new MusicalPieceDTO(musicalPiece.getName(),musicalPiece.getAuthor(),musicalPiece.getLength()));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPieceDTOList,httpHeaders, HttpStatus.OK);

    }

    @Operation(summary = "Retrieve a musical Piece by name",
            description = "The response is a list of Musical Piece",
            tags = {"name"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MusicalPiece.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<List<MusicalPieceDTO>> musicalPieceActiveByName(@PathVariable String name) {

        Set<MusicalPiece> musicalPieceSet = new HashSet<>(musicalPieceRepository.getByNameLikeAndActiveTrue(name));
        List<MusicalPiece> musicalPieceList = new ArrayList<>(musicalPieceSet);
        List<MusicalPieceDTO> musicalPieceDTOList = new ArrayList<>();
        for(MusicalPiece musicalPiece: musicalPieceList){
            musicalPieceDTOList.add(new MusicalPieceDTO(musicalPiece.getName(),musicalPiece.getAuthor(),musicalPiece.getLength()));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPieceDTOList,httpHeaders, HttpStatus.OK);

    }

}