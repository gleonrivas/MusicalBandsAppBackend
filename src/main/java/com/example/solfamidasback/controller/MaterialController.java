package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.BorrowedMaterialDTO;
import com.example.solfamidasback.controller.DTO.BorrowedMaterialUpdateDTO;
import com.example.solfamidasback.controller.DTO.MaterialDTO;
import com.example.solfamidasback.model.Material;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.MaterialRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MaterialService materialService;

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

    @PostMapping("/create")
    public ResponseEntity<Material> createMaterial(@RequestBody MaterialDTO materialDTO){
        Material material = materialService.createUpdate(materialDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(material,httpHeaders, HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity<Material> updateMaterial(@RequestBody MaterialDTO materialDTO){
        Material material = materialService.createUpdate(materialDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(material,httpHeaders, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{idMaterial}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Integer idMaterial){
        String result = materialService.deleteMaterial(idMaterial);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(result,httpHeaders, HttpStatus.OK);
    }



}

