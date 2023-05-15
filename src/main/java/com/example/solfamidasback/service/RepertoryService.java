package com.example.solfamidasback.service;

import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.DTO.RepertoryDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Repertory;
import com.example.solfamidasback.model.converter.RepertoryConverter;
import com.example.solfamidasback.repository.CalendarEventRepository;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.repository.RepertoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepertoryService {
    @Autowired
    RepertoryConverter repertoryConverter;
    @Autowired
    RepertoryRepository repertoryRepository;
    @Autowired
    FormationRepository formationRepository;
    @Autowired
    CalendarEventRepository calendarRepository;

    public List<RepertoryDTO> findByIdFormation(Integer idFormation) {
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        Set<Repertory> repertoryList = formation.getRepertorySet();
        List<Repertory> repertories = new ArrayList<>(repertoryList);
        List<RepertoryDTO> repertoryDTOList = new ArrayList<>();
        for (Repertory repertory : repertories) {
            if (repertory.isActive()) {
                repertoryDTOList.add(repertoryConverter.toDTO(repertory));
            }
        }

        return repertoryDTOList.stream()
                .sorted(Comparator.comparing(RepertoryDTO::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<RepertoryDTO> findById() {
        Set<Repertory> repertoryList = repertoryRepository.findAllByActiveIsTrue();
        List<Repertory> repertories = new ArrayList<>(repertoryList);
        List<RepertoryDTO> repertoryDTOList = new ArrayList<>();
        for (Repertory repertory : repertories) {
            repertoryDTOList.add(repertoryConverter.toDTO(repertory));
        }

        return repertoryDTOList;
    }

    public RepertoryDTO findUniqueById(Integer id) {
        Repertory repertory = repertoryRepository.findByIdAndActiveIsTrue(id);
        RepertoryDTO repertoryDTO = new RepertoryDTO();
        if (repertory != null) {
            repertoryDTO = repertoryConverter.toDTO(repertory);
        }

        return repertoryDTO;
    }

    public Repertory createUpdate(RepertoryDTO repertoryDTO) {
        Repertory repertory = new Repertory();
        if (repertoryDTO.getId() == null) {
            repertory = repertoryConverter.toEntity(repertoryDTO);
        } else {
            repertory = repertoryRepository.findByIdAndActiveIsTrue(repertoryDTO.getId());
            repertory.setName(repertoryDTO.getName());
            repertory.setDescription(repertoryDTO.getDescription());
        }

        repertoryRepository.save(repertory);
        return repertory;
    }

    public String modifyActive(Integer id) {
        Repertory repertory = repertoryRepository.findByIdAndActiveIsTrue(id);
        String active = "";
        if (repertory != null) {
            repertory.setActive(false);
            repertoryRepository.save(repertory);
            active = "deleted";

        } else {
            active = "null";
        }

        return active;
    }

    public RepertoryDTO findByIdCalendar(Integer idCalendar) {
        RepertoryDTO repertoryDTO = new RepertoryDTO();
        CalendarEvent calendarEvent = calendarRepository.findFirstById(idCalendar);
        if(calendarEvent != null){
            Repertory repertory = repertoryRepository.findByActiveIsTrueAndCalendarEvent(calendarEvent);
            if(repertory!= null){
                repertoryDTO = repertoryConverter.toDTO(repertory);
                return repertoryDTO;
            }
        }


        return repertoryDTO;
    }

}
