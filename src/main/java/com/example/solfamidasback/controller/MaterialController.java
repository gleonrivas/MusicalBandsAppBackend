package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.BorrowedMaterialDTO;
import com.example.solfamidasback.model.Material;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.MaterialRepository;
import com.example.solfamidasback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    MaterialRepository materialRepository;
    @Autowired
    UserRepository userRepository;

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
            return new ResponseEntity("created borrowed material",headers, HttpStatus.OK);
        }
        return new ResponseEntity("borrowed material it already exists or error",headers, HttpStatus.OK);
    }

}

