package com.example.solfamidasback.model.converter;

import com.example.solfamidasback.controller.DTO.MaterialDTO;
import com.example.solfamidasback.model.Material;


public interface IMaterialConverter {



    MaterialDTO toDTO(Material material);

    Material toEntity(MaterialDTO materialDTO);
}
