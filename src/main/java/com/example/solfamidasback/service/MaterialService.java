package com.example.solfamidasback.service;

import com.example.solfamidasback.controller.DTO.MaterialDTO;
import com.example.solfamidasback.model.Enums.EnumMaterialType;
import com.example.solfamidasback.model.Material;
import com.example.solfamidasback.model.converter.MaterialConverter;
import com.example.solfamidasback.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class MaterialService {

    @Autowired
    MaterialConverter materialConverter;
    @Autowired
    MaterialRepository materialRepository;

    public Material createUpdate(MaterialDTO materialDTO){
        Material material = new Material();
        if (materialDTO.getId() == null){
            material = materialConverter.toEntity(materialDTO);
        }else {
            material = materialRepository.findByIdAndActiveIsTrue(materialDTO.getId());
            material.setTransferredMaterial(materialDTO.getTransferredMaterial());
            material.getFormation().setId(materialDTO.getIdFormation());
            material.setFullDate((LocalDate.parse(materialDTO.getFullDate())).atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDateTime());
            material.setMaterialType(EnumMaterialType.values()[materialDTO.getMaterialType()]);
        }

        materialRepository.save(material);
        return material;
    }

    public String deleteMaterial(Integer id){
        Material material= materialRepository.findByIdAndActiveIsTrue(id);
        String active;
        if (material!=null){
            material.setActive(false);
            materialRepository.save(material);
            active = "Deleted successfully";
        }else {
            active = "Delete Failed";
        }

        return active;
    }

}
