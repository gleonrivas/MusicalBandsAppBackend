package com.example.solfamidasback.service;

import com.example.solfamidasback.model.Enums.EnumRolUser;
import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.model.UserFormationRole;
import com.example.solfamidasback.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role createRoleFormationAdministrator() {
        List<UserFormationRole> userFormationRoleList = new ArrayList<>();

        Role rol = roleRepository.findFirstOrderByIdDesc();
        Integer idNewRol = rol.getId() + 1;

        return new Role(idNewRol, true, EnumRolUserFormation.OWNER, userFormationRoleList);

    }
}
