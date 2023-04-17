package com.example.solfamidasback.service;

import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormationService {
    @Autowired
    FormationRepository formationRepository;

    public String deleteUserFormation(Integer formationID, Integer userId){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(formationID);
        formationRepository.deleteUserFormation(userId,formation.getName());
        Integer formationId = formationRepository.findFormationUserFalse(userId);

        if (formationID != null){
            return "User Deleted From Formation OK";
        }else {
            return "Error User Deleted From Formation";
        }
    }

}
