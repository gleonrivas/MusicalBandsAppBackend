package com.example.solfamidasback.service;

import com.example.solfamidasback.model.DTO.InvitationLinkDTO;
import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.RoleRepository;
import com.example.solfamidasback.repository.UserFormationRoleRepository;
import com.example.solfamidasback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormationService {
    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UserFormationRoleRepository userFormationRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

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

    public String addingByInvitationLink(InvitationLinkDTO invitationLinkDTO, Users user){
        //buscar formacion por invitacion
        Formation formation = formationRepository.findByLinkAndActiveIsTrue(invitationLinkDTO.getLink());
        List<Formation> formationList = formationRepository.getAllByUserAndActiveIsTrue(user.getId());
        List<Formation> formationListOwner = formationRepository.getAllByUserOwnerAndActiveIsTrue(user.getId());
            //si no encuentra, respuesta no existe
        if(formation == null){
            return "you can't log in this formation";
        } else if (formationList.contains(formation) || formationListOwner.contains(formation)) {
            return "you are a member already";
        } else{
            //si encuentra, crear el rol base y se crea la relación user_formation_rol
            Role role = new Role(true, EnumRolUserFormation.COMPONENT);
            roleRepository.save(role);
            role = roleRepository.findFirstOrderByIdDesc();
            UserFormationRole userFormationRole = new UserFormationRole(user,formation,role,true);
            userFormationRole.setActive(true);
            userFormationRoleRepository.save(userFormationRole);
            return "you have been added succesfully";
        }
    }






}
