package com.example.solfamidasback.service;

import com.example.solfamidasback.model.DTO.InvitationLinkDTO;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.repository.FormationRepository;
import com.example.solfamidasback.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationLinkService {
    @Autowired
    FormationRepository formationRepository;

    public InvitationLinkDTO createNewInvitation(Integer idFormation){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        String informationLink = Utilities.createLink();
        formation.setLink(informationLink);
        formationRepository.save(formation);
        InvitationLinkDTO invitationLinkDTO = new InvitationLinkDTO(informationLink);
        return invitationLinkDTO;
    }

    public String deleteInvitation(Integer idFormation){
        Formation formation = formationRepository.findFormationByIdAndActiveIsTrue(idFormation);
        formation.setLink(null);
        formationRepository.save(formation);
        return  "invitation link removed";
    }
}
