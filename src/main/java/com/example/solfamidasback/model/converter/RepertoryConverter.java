package com.example.solfamidasback.model.converter;

import com.example.solfamidasback.model.DTO.RepertoryDTO;
import com.example.solfamidasback.model.Repertory;
import com.example.solfamidasback.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepertoryConverter implements IRepertoryConverter{

    @Autowired
    FormationRepository formationRepository;


    @Override
    public RepertoryDTO toDTO(Repertory repertory) {

        RepertoryDTO repertoryDTO = new RepertoryDTO();
        repertoryDTO.setId(repertory.getId());
        repertoryDTO.setName(repertory.getName());
        repertoryDTO.setDescription(repertory.getDescription());

        return repertoryDTO;
    }

    @Override
    public Repertory toEntity(RepertoryDTO repertoryDTO) {
        Repertory repertory = new Repertory();
        repertory.setId(repertoryDTO.getId());
        repertory.setDescription(repertoryDTO.getDescription());
        repertory.setName(repertoryDTO.getName());
        repertory.setFormation(formationRepository.findFormationByIdAndActiveIsTrue(repertoryDTO.getIdFormation()));
        return repertory;
    }

}
