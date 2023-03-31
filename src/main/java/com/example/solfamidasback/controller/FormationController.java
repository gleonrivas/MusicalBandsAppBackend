package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.FormationDTO;
import com.example.solfamidasback.controller.DTO.FormationUpdateDTO;
import com.example.solfamidasback.model.Enums.EnumFormationType;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Users;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("formation")
public class FormationController {

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/listByUser/{user_id}")
    public @ResponseBody List<Formation> listFormationByUserAndActive(@PathVariable Integer user_id) throws JsonProcessingException {

        return formationRepository.findAllByUserAndActiveIsTrue(user_id);
    }

    @GetMapping("/listById/{formation_id}")
    public @ResponseBody Formation formationById(@PathVariable Integer formation_id) throws JsonProcessingException {
        return formationRepository.findFormationByIdAndActiveIsTrue(formation_id);
    }

    @PostMapping("/create")
    public String createFormation(@RequestBody FormationDTO formationDTO) {
        Users user = userRepository.findByIdAndActiveIsTrue(formationDTO.getId_user());

        Formation formation = new Formation();
        formation.setActive(true);
        formation.setLogo(formationDTO.getLogo());
        formation.setName(formationDTO.getName());
        formation.setDesignation(formationDTO.getDesignation());
        formation.setType(formationDTO.getType());
      //aquí va el formation type que tengo que castearlo al enum
        formation.setFundationDate(LocalDateTime.parse(formationDTO.getFundationDate()));
        formation.setUsers(user);
        formationRepository.save(formation);
        //bucar la formación para coger el id
        return "Formation created";
    }

    @PostMapping("/update")
    public String updateFormation(@RequestBody FormationUpdateDTO formationupdateDTO) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(formationupdateDTO.getId());
        Users user = userRepository.findByIdAndActiveIsTrue(formationupdateDTO.getId_user());
        formation.setActive(true);
        formation.setLogo(formationupdateDTO.getLogo());
        formation.setName(formationupdateDTO.getName());
        formation.setDesignation(formationupdateDTO.getDesignation());
        formation.setType(formationupdateDTO.getType());
        formation.setFundationDate(LocalDateTime.parse(formationupdateDTO.getFundationDate().replace(" ", "T"), formatter));
        formation.setUsers(user);

        formationRepository.save(formation);
        //bucar la formación para coger el id
        return "Formation created";
    }



    @DeleteMapping("/delete/{id_formation}")
    public String deleteFormation(@PathVariable Integer id_formation) {

        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(id_formation);
        formation.setActive(false);
        formationRepository.save(formation);
        return "formation deleted";

    }

}
