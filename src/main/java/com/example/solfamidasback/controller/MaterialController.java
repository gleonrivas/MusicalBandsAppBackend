package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.BorrowedMaterialDTO;
import com.example.solfamidasback.controller.DTO.BorrowedMaterialUpdateDTO;
import com.example.solfamidasback.controller.DTO.MaterialDTO;
import com.example.solfamidasback.controller.DTO.UserDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Material;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.model.converter.MaterialConverter;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.MaterialRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.FormationService;
import com.example.solfamidasback.service.MaterialService;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "Material", description = "Material crud and Material Borrowed crud")
@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MaterialService materialService;
    @Autowired
    FormationRepository formationRepository;
    @Autowired
    MaterialConverter materialConverter;

    @Operation(summary = "creates a borrowed material indicating the material id and the user id",
            description = "The response is a String if an error has been created or raised")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/createBorrowedMaterial")
    public ResponseEntity<String> saveBorrowedMaterial(@RequestBody BorrowedMaterialDTO borrowedMaterialDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Integer> bm = materialRepository.findBorrowedMaterial(borrowedMaterialDTO.getMaterialId(), borrowedMaterialDTO.getUserId());
        Optional<Material> material = materialRepository.findById(borrowedMaterialDTO.getMaterialId());
        Users users = userRepository.findByIdAndActiveIsTrue(borrowedMaterialDTO.getUserId());

        if (material == null || users == null){
            return new ResponseEntity("material or user not exists or error",headers, HttpStatus.OK);
        }
        if (bm.isEmpty()){
            materialRepository.createBorrowedMaterial(borrowedMaterialDTO.getMaterialId(), borrowedMaterialDTO.getUserId());
            return new ResponseEntity("successfully created borrowed material",headers, HttpStatus.OK);
        }
        return new ResponseEntity("borrowed material it already exists or error",headers, HttpStatus.OK);
    }


    @Operation(summary = "Modify the material borrowed passing old material id, new material id and the user id",
            description = "The response is a String if an error has been updated or raised")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/updateMaterialBorrowedMaterial")
    public ResponseEntity<String> updateBorrowedMaterial(@RequestBody BorrowedMaterialUpdateDTO borrowedMaterialDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Optional<Material> material = materialRepository.findById(borrowedMaterialDTO.getMaterialId());
        Users users = userRepository.findByIdAndActiveIsTrue(borrowedMaterialDTO.getUserId());
        List<Integer> bm = materialRepository.findBorrowedMaterial(borrowedMaterialDTO.getMaterialId(), borrowedMaterialDTO.getUserId());
        Optional<Material> material2 = materialRepository.findById(borrowedMaterialDTO.getNewMaterialId());


        if (material.isEmpty() || users == null || material2.isEmpty()){
            return new ResponseEntity("material or user not exists or error",headers, HttpStatus.OK);
        }

        if (borrowedMaterialDTO.getNewMaterialId() != null ){
            materialRepository.changeMaterial(borrowedMaterialDTO.getNewMaterialId(), borrowedMaterialDTO.getUserId(), borrowedMaterialDTO.getMaterialId());
            return new ResponseEntity("successfully update borrowed material",headers, HttpStatus.OK);
        }


        return new ResponseEntity("borrowed material it already exists or error",headers, HttpStatus.OK);
    }

    @Operation(summary = "Modify the material borrowed passing old user id, new user id and the material id",
            description = "The response is a String if an error has been updated or raised")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/updateUserBorrowedMaterial")
    public ResponseEntity<String> updateUserBorrowedMaterial(@RequestBody BorrowedMaterialUpdateDTO borrowedMaterialDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Optional<Material> material = materialRepository.findById(borrowedMaterialDTO.getMaterialId());
        Users users = userRepository.findByIdAndActiveIsTrue(borrowedMaterialDTO.getUserId());
        List<Integer> bm = materialRepository.findBorrowedMaterial(borrowedMaterialDTO.getMaterialId(), borrowedMaterialDTO.getUserId());
        Users users2 = userRepository.findByIdAndActiveIsTrue(borrowedMaterialDTO.getNewUserId());



        if (material.isEmpty() || users == null || users2 == null){
            return new ResponseEntity("material or user not exists or error",headers, HttpStatus.OK);
        }

        if (borrowedMaterialDTO.getNewUserId() != null ){
            materialRepository.changeUser(borrowedMaterialDTO.getNewUserId(), borrowedMaterialDTO.getMaterialId(), borrowedMaterialDTO.getUserId());
            return new ResponseEntity("successfully update borrowed material",headers, HttpStatus.OK);
        }


        return new ResponseEntity("borrowed material it already exists or error",headers, HttpStatus.OK);
    }

    @Operation(summary = "Delete the material borrowed passing user id and the material id",
            description = "The response is a String if an error has been deleted or raised")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/deleteBorrowedMaterial")
    public ResponseEntity<String> deleteBorrrowedMarial(@RequestBody BorrowedMaterialDTO borrowedMaterialDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Integer> bm = materialRepository.findBorrowedMaterial(borrowedMaterialDTO.getMaterialId(), borrowedMaterialDTO.getUserId());
        if (bm.isEmpty()){
            return new ResponseEntity("borrowed material not exist",headers, HttpStatus.OK);
        }
        materialRepository.deleteBorrowedMaterial(borrowedMaterialDTO.getMaterialId(), borrowedMaterialDTO.getUserId());

        return new ResponseEntity("successfully delete borrowed material",headers, HttpStatus.OK);
    }

    @Operation(summary = "Create a material",
            description = "Create material with id, transferredMaterial, materialType, fullDate and idFormation")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MaterialDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/create")
    public ResponseEntity<Material> createMaterial(@RequestBody MaterialDTO materialDTO){
        Material material = materialService.createUpdate(materialDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(material,httpHeaders, HttpStatus.OK);

    }

    @Operation(summary = "Update a material",
            description = "Update material with id, transferredMaterial, materialType, fullDate and idFormation")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = MaterialDTO.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @PutMapping("/update")
    public ResponseEntity<Material> updateMaterial(@RequestBody MaterialDTO materialDTO){
        Material material = materialService.createUpdate(materialDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(material,httpHeaders, HttpStatus.OK);

    }

    @Operation(summary = "Delete a material",
            description = "Delete material with id material")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = String.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
    })
    @DeleteMapping("/delete/{idMaterial}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Integer idMaterial){
        String result = materialService.deleteMaterial(idMaterial);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(result,httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Receive list of materials",
            description = "Receive all the materials of a formation using the id of the formation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = MaterialDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
    })
    @GetMapping("/listMaterialIdFormation/{idFormation}")
    public ResponseEntity<List<MaterialDTO>> getListMaterialIdFormation(@PathVariable Integer idFormation){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        List<MaterialDTO> listMaterialDTO = new ArrayList<>();
        if (formation != null){
            List<Material> listMaterial = materialRepository.getAllByIdFormation(idFormation);
            for (Material material : listMaterial){
            MaterialDTO materialDTO = materialConverter.toDTO(material);
            listMaterialDTO.add(materialDTO);
            }
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(listMaterialDTO,httpHeaders, HttpStatus.OK);
    }

  @GetMapping("/getAllMaterialBorroweUser")
    ResponseEntity<List<MaterialDTO>> getAllMaterialUser(@RequestBody BorrowedMaterialDTO borrowedMaterialDTO){
        Users users = userRepository.findByIdAndActiveIsTrue(borrowedMaterialDTO.getUserId());
        List<Integer> listMaterialId = materialRepository.getAllMateriaLWhereUserId(borrowedMaterialDTO.getUserId());
        if (users == null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(users,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        List<Material> materialList = new ArrayList<>();
        List<MaterialDTO> materialDTOS = new ArrayList<>();
        for (Integer id : listMaterialId){
            Material material = materialRepository.findByIdAndActiveIsTrue(id);
            materialList.add(material);
        }
        for (Material material : materialList){
            MaterialDTO materialDTO = materialConverter.toDTO(material);
            materialDTOS.add(materialDTO);
        }
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
      return new ResponseEntity(materialDTOS,httpHeaders, HttpStatus.OK);
    }
    public String convertDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = date.format(formatter);
        return  formattedString;
    }

    @GetMapping("/getAllUserBorrorwedMaterial")
    ResponseEntity<List<UserDTO>> getAllUserMaterial(@RequestBody BorrowedMaterialDTO borrowedMaterialDTO){
        Material material = materialRepository.findByIdAndActiveIsTrue(borrowedMaterialDTO.getMaterialId());
        if (material == null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity(material,httpHeaders, HttpStatus.BAD_REQUEST);
        }
        List<Integer> listUserId = materialRepository.getAllUserWhereMaterialId(borrowedMaterialDTO.getMaterialId());
        List<Users> usersList = new ArrayList<>();
        List<UserDTO> userDTOList = new ArrayList<>();

        for (Integer id : listUserId){
            Users users = userRepository.findByIdAndActiveIsTrue(id);
            usersList.add(users);
        }
        for (Users users : usersList){
            UserDTO  userDTO = new UserDTO();
            userDTO.setId(users.getId());
            userDTO.setEmail(users.getEmail());
            userDTO.setName(users.getName());
            userDTO.setSurName(users.getSurName());
            userDTOList.add(userDTO);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(userDTOList,httpHeaders, HttpStatus.OK);
    }


}

