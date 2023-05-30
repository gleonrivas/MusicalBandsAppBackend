package com.example.solfamidasback.controller;


import com.example.solfamidasback.controller.DTO.*;

import com.example.solfamidasback.model.*;
import com.example.solfamidasback.model.DTO.CalendarDTO;
import com.example.solfamidasback.model.DTO.ExternalMusicianDTO;
import com.example.solfamidasback.model.DTO.PayLowDTO;
import com.example.solfamidasback.model.DTO.UserPaidDTO;
import com.example.solfamidasback.repository.*;
import com.example.solfamidasback.service.JwtService;
import com.example.solfamidasback.service.TreasuryService;
import com.example.solfamidasback.utilities.Utilities;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.text.pdf.PdfWriter;



import java.io.ByteArrayOutputStream;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Treasury", description = "Treasury logic")
@RestController
@RequestMapping("/treasury")
public class TreasuryController {
    @Autowired
    TreasuryRepository treasuryRepository;
    @Autowired
    TreasuryService treasuryService;
    @Autowired
    FormationRepository formationRepository;
    @Autowired
    ExternalMusicianRepository externalMusicianRepository;
    @Autowired
    CalendarEventRepository calendarEventRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    @Operation(summary = "Shows a list of events",
            description = "Shows all the events of a formation that are paid")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CalendarEvent.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @GetMapping("/getAllEvents")
    public ResponseEntity<List<CalendarEvent>> getAllEvents (@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation == null){
            List<CalendarEvent> calendarEventList = new ArrayList<>();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(calendarEventList,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        List<CalendarEvent> calendarEventList = treasuryService.eventsWithMoney(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(calendarEventList,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Shows a list of musicians",
            description = "Shows all the external musicians that the group has had in the different performances")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExternalMusician.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @GetMapping("/getAllExternalMusician")
    public ResponseEntity<List<ExternalMusician>> getAllExternalMusician (@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation == null){
            List<ExternalMusician> externalMusicians = new ArrayList<>();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(externalMusicians,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        List<ExternalMusician> externalMusicianList = treasuryService.externalMusicianEvents(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(externalMusicianList,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Action of paying an external musician",
            description = "Pay the external musician the amount of the gig, subtracting it from the total money of the formation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @PostMapping("/payMusician")
    public ResponseEntity<ExternalMusician> payExternalMusician(@RequestBody ExternalMusicianDTO externalMusicianDTO){
        ExternalMusician externalMusician = externalMusicianRepository.findExternalMusicianByIdAndActiveIsTrue(externalMusicianDTO.getExternalMusicianId());
        if (externalMusician == null){
            ExternalMusician externalMusicians = new ExternalMusician();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(externalMusicians,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        treasuryService.externalMusicianPaid(externalMusician);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(new ResponseStringDTO("Se ha pagado al músico"),httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Action to collect a performance",
            description = "Sets the event as paid and adds it to the total amount of the training")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @PostMapping("/payEvent")
    public ResponseEntity<CalendarEvent> payEvent(@RequestBody CalendarDTO calendarDTO){
        CalendarEvent calendarEvent = calendarEventRepository.findCalendarEventById(calendarDTO.getCalendarId());
        treasuryService.calendarEventPaid(calendarEvent);
        if (calendarEvent == null){
            CalendarEvent calendarEvent1 = new CalendarEvent();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(calendarEvent1,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(new ResponseStringDTO("Se ha recibido el dinero del evento"),httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Pay a user who unsubscribes",
            description = "Go through all the events that the user has attended, check if they have penalties and get the total amount that the user receives and stays in the formation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserPaidDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @PostMapping("/payLow")
    public ResponseEntity<UserPaidDTO> payLowUser(@NotNull @RequestBody PayLowDTO payLowDTO){
        Users users = userRepository.findByIdAndActiveIsTrue(payLowDTO.getUserId());
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (users == null || formation == null){
            UserPaidDTO userPaidDTO = new UserPaidDTO();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(userPaidDTO,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        UserPaidDTO userPaid = treasuryService.paidUserFormation(users,formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(userPaid,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Makes the annual account of training",
            description = "It makes the annual account of the training, distributing to all the users the amounts that belong to it and shows it in json")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = PayFormationDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @PostMapping("/payFormationJson")
    public ResponseEntity<PayFormationDTO> payFormation(@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation == null){
            PayFormationDTO payFormationDTO = new PayFormationDTO();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(payFormationDTO,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        PayFormationDTO payFormationDTO = treasuryService.calculatePaidFormation(formation);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(payFormationDTO,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Makes the annual account of training",
            description = "It makes the annual account of the training, distributing to all the users the amounts that belong to it and shows it in pdf")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = PayFormationDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @PostMapping("/payFormationPdf")
    public ResponseEntity<byte[]> payFormation2(@RequestBody PayLowDTO payLowDTO) {
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation == null) {
            return ResponseEntity.badRequest().build();
        }

        PayFormationDTO payFormationDTO = treasuryService.calculatePaidFormation(formation);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph centeredParagraph = new Paragraph();
            centeredParagraph.setAlignment(Element.ALIGN_CENTER);
            centeredParagraph.add(new Phrase("Information: " + formation.getName() + " " + payFormationDTO.getPayDay()));
            document.add(centeredParagraph);
            document.add(new Chunk(new LineSeparator(1, 100, BaseColor.BLACK, Element.ALIGN_CENTER, -5)));

            for (UsersPaidDTO userPaidDTO : payFormationDTO.getUsersPaid()) {
                document.add(new Paragraph("Nombre: " + userPaidDTO.getName()));
                document.add(new Paragraph("Apellido: " + userPaidDTO.getSubname()));
                document.add(new Paragraph("Cantidad descontada: " + userPaidDTO.getAmountPenalty() + " €"));
                document.add(new Paragraph("Cantidad recibida: " + userPaidDTO.getAmountReceibes() + " €"));
                document.add(new Chunk(new LineSeparator(1, 0, BaseColor.BLACK, Element.ALIGN_CENTER, -5)));
            }
            document.add(new Chunk(new LineSeparator(1, 0, BaseColor.BLACK, Element.ALIGN_CENTER, -5)));

            document.add(new Paragraph("Cantidad total pagada: " + payFormationDTO.getTotalPaid() + " €"));
            document.add(new Chunk(new LineSeparator(1, 100, BaseColor.BLACK, Element.ALIGN_CENTER, -5)));
            document.add(new Paragraph("Cantidad total en cuenta: " + payFormationDTO.getInAccount() + " €"));

            document.close();


            byte[] pdfBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "payFormation.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Shows the money that the formation has",
            description = "Shows the money that the formation has, with the date and if the total amount has increased or decreased")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Treasury.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @GetMapping("/getAllMoney")
    ResponseEntity<List<Treasury>> getAllMoney(@RequestBody PayLowDTO payLowDTO){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(payLowDTO.getFormationId());
        if (formation==null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(formation,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        List<Treasury> treasuryList = treasuryRepository.getAllTreasuryFormation(payLowDTO.getFormationId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(treasuryList,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Shows if the user is super admin",
            description = "Through a token, check if the user is super admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @GetMapping("/isSuperAdmin")
    ResponseEntity<String> isSuperAdmin(HttpServletRequest request){
        boolean isSuper = Utilities.isSuperAdministrador(request,jwtService,userRepository);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(isSuper,httpHeaders, HttpStatus.OK);
    }
}
