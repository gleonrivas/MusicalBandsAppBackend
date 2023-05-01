package com.example.solfamidasback.model.converter;

import com.example.solfamidasback.model.DTO.RepertoryDTO;
import com.example.solfamidasback.model.Repertory;

public interface IRepertoryConverter {

    RepertoryDTO toDTO(Repertory repertory);

    Repertory toEntity(RepertoryDTO repertoryDTO);



}
