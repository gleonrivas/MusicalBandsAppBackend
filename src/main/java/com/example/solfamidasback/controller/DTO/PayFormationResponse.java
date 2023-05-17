package com.example.solfamidasback.controller.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayFormationResponse {
    private PayFormationDTO payFormationDTO;
    private String pdfBase64;
}
