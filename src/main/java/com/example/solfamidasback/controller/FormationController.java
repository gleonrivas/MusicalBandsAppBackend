package com.example.solfamidasback.controller;

import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller+
@RequestMapping("formation")
public class FormationController {

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UserService userService;

    @GetMapping("/listByUser/{user_id}")
    public @ResponseBody List<Formation> listFormationByUserAndActive(@PathVariable Integer user_id) throws JsonProcessingException {

        return formationRepository.findAllByUserAndActiveIsTrue(user_id);
    }


}
