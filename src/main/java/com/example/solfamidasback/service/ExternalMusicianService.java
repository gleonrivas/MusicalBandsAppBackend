package com.example.solfamidasback.service;

import com.example.solfamidasback.controller.DTO.ExternalMusicianDTO;
import com.example.solfamidasback.controller.DTO.ExternalMusicianUpdateDTO;
import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.ExternalMusician;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.ExternalMusicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalMusicianService {

    @Autowired
    ExternalMusicianRepository externalMusicianRepository;
    @Autowired
    CalendarEventRepository calendarEventRepository;

    public List<ExternalMusician> findByCalendar(Integer idCalendar){
        CalendarEvent calendar = calendarEventRepository.findFirstById(idCalendar);
        return externalMusicianRepository.findAllByActiveIsTrueAndCalendar(calendar);
    }

    public List<ExternalMusician> findByName(String name){
        return externalMusicianRepository.getByNameAndActiveIsTrue(name);
    }

    public Optional<ExternalMusician> findById (Integer id){
        return externalMusicianRepository.findById(id);
    }

    public String deleteById(Integer id){
        ExternalMusician externalMusician = externalMusicianRepository.findFirstByIdAndActiveIsTrue(id);
        if(externalMusician!=null){
            externalMusician.setActive(false);
            externalMusicianRepository.save(externalMusician);
            return "Deleted succefully";
        }
        return "Not found";
    }

    public ExternalMusician create(ExternalMusicianDTO externalMusicianDTO){
        CalendarEvent calendarEvent = calendarEventRepository.findFirstById(externalMusicianDTO.getIdCalendar());
        ExternalMusician externalMusician = new ExternalMusician();
        externalMusician.setDni(externalMusicianDTO.getDni());
        externalMusician.setEmail(externalMusicianDTO.getEmail());
        externalMusician.setAmount(externalMusicianDTO.getAmount());
        externalMusician.setName(externalMusicianDTO.getName());
        externalMusician.setSurname(externalMusicianDTO.getSurname());
        externalMusician.setBankAccount(externalMusicianDTO.getBankAccount());
        externalMusician.setCalendar(calendarEvent);
        externalMusician.setPhone(externalMusicianDTO.getPhone());
        externalMusician.setActive(true);
        externalMusicianRepository.save(externalMusician);
        return externalMusician;
    }

    public ExternalMusician update(ExternalMusicianUpdateDTO externalMusicianUpdateDTO){
        CalendarEvent calendarEvent = calendarEventRepository.findFirstById(externalMusicianUpdateDTO.getIdCalendar());
        ExternalMusician externalMusician = externalMusicianRepository.findFirstByIdAndActiveIsTrue(externalMusicianUpdateDTO.getId());
        externalMusician.setId(externalMusicianUpdateDTO.getId());
        externalMusician.setDni(externalMusicianUpdateDTO.getDni());
        externalMusician.setEmail(externalMusicianUpdateDTO.getEmail());
        externalMusician.setAmount(externalMusicianUpdateDTO.getAmount());
        externalMusician.setName(externalMusicianUpdateDTO.getName());
        externalMusician.setSurname(externalMusicianUpdateDTO.getSurname());
        externalMusician.setBankAccount(externalMusicianUpdateDTO.getBankAccount());
        externalMusician.setCalendar(calendarEvent);
        externalMusician.setPhone(externalMusicianUpdateDTO.getPhone());
        externalMusicianRepository.save(externalMusician);
        return externalMusician;
    }



}
