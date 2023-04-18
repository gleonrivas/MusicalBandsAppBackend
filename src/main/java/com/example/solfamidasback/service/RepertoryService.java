package com.example.solfamidasback.service;

import com.example.solfamidasback.model.DTO.RepertoryDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Repertory;
import com.example.solfamidasback.model.converter.RepertoryConverter;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.RepertoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RepertoryService {
    @Autowired
    RepertoryConverter repertoryConverter;
    @Autowired
    RepertoryRepository repertoryRepository;
    @Autowired
    FormationRepository formationRepository;
    public List<RepertoryDTO> findByIdFormation (Integer idFormation){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        Set<Repertory> repertoryList = repertoryRepository.findAllByFormationAndActiveIsTrue(formation);
        List<RepertoryDTO> repertoryDTOList = new ArrayList<>();
        for(Repertory repertory: repertoryList){
            repertoryConverter.toDTO(repertory);
            repertoryList.add(repertory);
        }
        return repertoryDTOList;
    }


}
