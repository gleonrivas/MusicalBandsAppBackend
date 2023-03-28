package com.example.solfamidasback.controller;

import com.example.solfamidasback.controller.DTO.FormationDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.UserRepository;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public @ResponseBody Formation formationById (@PathVariable Integer formation_id) throws JsonProcessingException{
        return formationRepository.findFormationByIdAndActiveIsTrue(formation_id);
    }

    @PostMapping("/create")
    public String createFormation (@RequestBody FormationDTO formationDTO){

        //Formation DTO
        //buscar usuario, si esxiste, crea formacion
        if(!userRepository.findByIdAndActiveIsTrue(formationDTO.getId_user())){
            return "this user doesn't exist";
        }else{
            //crear nueva formacion
            Formation formation = new Formation();
            formation.setActive(true);
            formation.setLogo(formationDTO.getLogo());
            formation.setName(formationDTO.getName());
            formation.setDesignation(formationDTO.getDesignation());
            formation.setType(formationDTO.getType());
            formation.setFundationDate(LocalDateTime.parse(formationDTO.getFundationDate()));
            formationRepository.save(formation);
            //bucar la formación para coger el id
            Integer id_formation = formationRepository.findLastFormation();
            //insertar en la tabla intermedia los valores
            formationRepository.insertMiddleTable(formationDTO.getId_user(), id_formation);
            return "User created";
        }

    }

    @DeleteMapping("/delete/{id_formation}")
    public String deleteFormation (@PathVariable Integer id_formation){

       Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(id_formation);
       formation.setActive(false);
       formationRepository.save(formation);
       return "formation deleted";

    }

}
