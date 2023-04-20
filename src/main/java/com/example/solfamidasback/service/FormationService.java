package com.example.solfamidasback.service;

import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.UserFormationRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormationService {
    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UserFormationRoleRepository userFormationRoleRepository;

    public String deleteUserFormation(Integer formationID, Integer userId){

        userFormationRoleRepository.deleteUserFormation(userId,formationID);
        Integer formationId = userFormationRoleRepository.findFormationUserFalse(userId, formationID);

        if (formationId != null){
            return "User Deleted From Formation OK";
        }else {
            return "Error User Deleted From Formation";
        }
    }

    public String reactiveUserFormation(Integer formationID, Integer userId){

        userFormationRoleRepository.reactiveUserFormation(userId,formationID);
        Integer formationId = userFormationRoleRepository.findFormationUserTrue(userId, formationID);

        if (formationId != null){
            return "User Reactive From Formation OK";
        }else {
            return "Error User Reactive From Formation";
        }
    }




}
