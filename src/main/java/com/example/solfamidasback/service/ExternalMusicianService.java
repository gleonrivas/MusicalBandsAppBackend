package com.example.solfamidasback.service;

import com.example.solfamidasback.model.ExternalMusician;
import com.example.solfamidasback.repository.ExternalMusicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalMusicianService {

    @Autowired
    ExternalMusicianRepository externalMusicianRepository;

    public List<ExternalMusician> findByCalendar(Integer idCalendar){
        return externalMusicianRepository.findAllByActiveIsTrueAndCalendar(idCalendar);
    }

    public List<ExternalMusician> findByName(String name){
        return externalMusicianRepository.findAllByNameAndActiveIsTrue(name);
    }


}
