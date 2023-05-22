package com.example.solfamidasback.model.converter;

import com.example.solfamidasback.controller.DTO.MaterialDTO;
import com.example.solfamidasback.model.Enums.EnumMaterialType;
import com.example.solfamidasback.model.Material;
import com.example.solfamidasback.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class MaterialConverter implements IMaterialConverter {
    @Autowired
    FormationRepository formationRepository;

    @Override
    public MaterialDTO toDTO(Material material) {

        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setId(material.getId());
        materialDTO.setTransferredMaterial(material.getTransferredMaterial());
        materialDTO.setFullDate(String.valueOf(material.getFullDate().toLocalDate()));
        materialDTO.setMaterialType(material.getMaterialType().ordinal());
        materialDTO.setIdFormation(material.getFormation().getId());

        return materialDTO;


    }

    @Override
    public Material toEntity(MaterialDTO materialDTO) {


        Material material = new Material();
        material.setId(materialDTO.getId());
        material.setTransferredMaterial(materialDTO.getTransferredMaterial());
        material.setMaterialType(EnumMaterialType.values()[materialDTO.getMaterialType()]);
        material.setFullDate((LocalDate.parse(materialDTO.getFullDate())).atStartOfDay().atZone(ZoneId.systemDefault()).toLocalDateTime());
        material.setFormation(formationRepository.findFormationByIdAndActiveIsTrue(materialDTO.getIdFormation()));

        return material;

    }
}
