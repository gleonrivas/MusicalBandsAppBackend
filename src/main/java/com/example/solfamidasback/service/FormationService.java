package com.example.solfamidasback.service;

import com.example.solfamidasback.controller.DTO.UsersFormationRoleDTO;
import com.example.solfamidasback.model.DTO.FormationDTO;
import com.example.solfamidasback.model.DTO.InvitationLinkDTO;
import com.example.solfamidasback.model.DTO.RepertoryDTO;
import com.example.solfamidasback.model.DTO.RoleDTO;
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

import java.util.*;

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

    public List<UsersFormationRoleDTO> listUsersByFormation(Integer idFormation){
        //buscar usuarios por formacion
        List<Users> userList = userRepository.getUsersByFormation(idFormation);
        //no se repiten
        Set<Users> usersSet = new HashSet<>(userList);
        userList = new ArrayList<>(usersSet);
        //ordenarlos
        List<Users> userFinalList = userList.stream().sorted(Comparator.comparing(Users::getId)).toList();
        List<UsersFormationRoleDTO> usersFormationRoleDTOList= new ArrayList<>();
        //rellenar userFormationRoleDTOList
        for(Users user:userFinalList){
            Set<Integer> roleTypeSet = userFormationRoleRepository.rolesByFormation(idFormation, user.getId());
            List<Integer> roleTypeList = new ArrayList<>(roleTypeSet);
            EnumRolUserFormation[] enumRolUserFormationList =  EnumRolUserFormation.values();
            List<RoleDTO> roleDTOList = new ArrayList<>();
            roleTypeList.stream().forEach(i -> roleDTOList.add(new RoleDTO(enumRolUserFormationList[i])));
            UsersFormationRoleDTO usersFormationRoleDTO = new UsersFormationRoleDTO(user.getId(),user.getName(),
                    user.getSurName(),user.getEmail(),"", roleDTOList);
            usersFormationRoleDTOList.add(usersFormationRoleDTO);
        }
        return usersFormationRoleDTOList;
    }

    public FormationDTO findByInvitationLink(String invitationLink){
        Formation formation = formationRepository.findByLinkAndActiveIsTrue(invitationLink);
        FormationDTO formationDTO = new FormationDTO();
        if(formation!= null){
            formationDTO.setName(formation.getName());
            formationDTO.setDesignation(formation.getDesignation());
            formationDTO.setType(formation.getType());
            if (formation.getFundationDate() != null) {
                formationDTO.setFundationDate(formation.getFundationDate().toString());
            }
            formationDTO.setLogo(formation.getLogo());
            return formationDTO;
        }else{
            return formationDTO;
        }

    }








}
