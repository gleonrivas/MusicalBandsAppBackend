package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.RepertoryMusicalPieceDTO;
import com.example.solfamidasback.model.*;
import com.example.solfamidasback.model.DTO.MusicalPieceDTO;
import com.example.solfamidasback.model.DTO.MusicalPieceUpdateDTO;
import com.example.solfamidasback.repository.MusicalPieceRepository;
import com.example.solfamidasback.repository.RepertoryRepository;
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
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private RepertoryRepository repertoryRepository;

    @Operation(summary = "Retrieve a list of musical Piece",
            description = "The response is a list of Musical Pieces")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MusicalPiece.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("")
    public ResponseEntity<List<MusicalPieceDTO>> listMusicalPieceActive(HttpServletRequest request) {

        List<MusicalPiece> musicalPieceList = musicalPieceRepository.findAllByActiveIsTrue();
        List<MusicalPieceDTO> musicalPieceDTOList = new ArrayList<>();
        for(MusicalPiece musicalPiece: musicalPieceList){
            MusicalPieceDTO musicalPieceDTO = new MusicalPieceDTO(musicalPiece.getName(),musicalPiece.getAuthor(), musicalPiece.getLength(), null);
            musicalPieceDTOList.add(musicalPieceDTO);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPieceDTOList,httpHeaders, HttpStatus.OK);
    }
    @Operation(summary = "Retrieve a list of musical Piece",
            description = "The response is a list of Musical Pieces")
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
            musicalPieceDTOList.add(new MusicalPieceDTO(musicalPiece.getName(),musicalPiece.getAuthor(),musicalPiece.getLength(), null));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPieceDTOList,httpHeaders, HttpStatus.OK);

    }

    @Operation(summary = "Retrieve a musical Piece by name",
            description = "The response is a list of Musical Pieces")
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
            musicalPieceDTOList.add(new MusicalPieceDTO(musicalPiece.getName(),musicalPiece.getAuthor(),musicalPiece.getLength(), null));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPieceDTOList,httpHeaders, HttpStatus.OK);

    }

    @Operation(summary = "Retrieve a musical Piece by author",
            description = "The response is a list of Musical Pieces")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MusicalPiece.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema()) }),
    })
    @GetMapping("/author/{author}")
    public ResponseEntity<List<MusicalPieceDTO>> musicalPieceActiveByAuthor(@PathVariable String author) {

        Set<MusicalPiece> musicalPieceSet = new HashSet<>(musicalPieceRepository.findAllByAuthorAndActiveIsTrue(author));
        List<MusicalPiece> musicalPieceList = new ArrayList<>(musicalPieceSet);
        List<MusicalPieceDTO> musicalPieceDTOList = new ArrayList<>();
        for(MusicalPiece musicalPiece: musicalPieceList){
            musicalPieceDTOList.add(new MusicalPieceDTO(musicalPiece.getName(),musicalPiece.getAuthor(),musicalPiece.getLength(), null));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPieceDTOList,httpHeaders, HttpStatus.OK);

    }
    @Operation(summary = "Create a musical piece",
            description = "Create a musical piece")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())}),
    })
    @PostMapping("/create")
    public ResponseEntity<MusicalPiece> createMusicalPiece(@RequestBody MusicalPieceDTO musicalPieceDTO) {
        Repertory repertory = repertoryRepository.findByIdAndActiveIsTrue(musicalPieceDTO.getIdRepertory());
        MusicalPiece musicalPiece = new MusicalPiece();
        musicalPiece.setName(musicalPieceDTO.getName());
        musicalPiece.setAuthor(musicalPieceDTO.getAuthor());
        musicalPiece.setLength(musicalPieceDTO.getLength());
        musicalPiece.setActive(true);
        musicalPieceRepository.save(musicalPiece);
        MusicalPiece musicalPieceCreated = musicalPieceRepository.findFirstByActiveIsTrueOrderByIdDesc();
        musicalPieceRepository.createRelationRepertoryMudicalPiece(repertory.getId(),musicalPieceCreated.getId(),true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPiece,headers, HttpStatus.OK);
    }

    @Operation(summary = "Update a musical Piece",
            description = "Uptate a musical Piece")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PutMapping("/update")
    public ResponseEntity<MusicalPiece> updateMusicalPiece(@RequestBody MusicalPieceUpdateDTO musicalPieceUpdateDTO) {
        MusicalPiece musicalPiece = musicalPieceRepository.findByIdAndActiveIsTrue(musicalPieceUpdateDTO.getId());
        musicalPiece.setName(musicalPieceUpdateDTO.getName());
        musicalPiece.setAuthor(musicalPieceUpdateDTO.getAuthor());
        musicalPiece.setLength(musicalPieceUpdateDTO.getLength());
        musicalPieceRepository.save(musicalPiece);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(musicalPiece,headers, HttpStatus.OK);
    }

    @Operation(summary = "Delete a musical piece by id",
            description = "Delete a musical piece by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MusicalPiece.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @DeleteMapping("/delete/{idMusicalPiece}")
    public ResponseEntity<String> deleteFormation(@PathVariable Long idMusicalPiece) {
        MusicalPiece musicalPiece = musicalPieceRepository.findByIdAndActiveIsTrue(idMusicalPiece);
        musicalPiece.setActive(false);
        musicalPieceRepository.save(musicalPiece);
        return ResponseEntity.ok("musical piece deleted");
    }

    @Operation(summary = "Delete a musical piece by id",
            description = "Delete a musical piece by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MusicalPiece.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })

    @DeleteMapping("/delete/{idMusicalPiece}/{idRepertory}")
    public ResponseEntity<String> deleteRelation(@PathVariable Integer idMusicalPiece, @PathVariable Integer idRepertory) {
        musicalPieceRepository.updateRelation(idRepertory,idMusicalPiece);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity("Musical piece deleted",headers, HttpStatus.OK);

    }
}
