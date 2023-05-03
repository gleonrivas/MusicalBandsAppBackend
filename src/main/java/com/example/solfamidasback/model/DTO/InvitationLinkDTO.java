package com.example.solfamidasback.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvitationLinkDTO {
    private String link;

    public InvitationLinkDTO(String link) {
        this.link = link;
    }
}
