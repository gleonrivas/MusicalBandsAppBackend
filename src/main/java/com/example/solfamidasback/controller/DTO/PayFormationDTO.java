package com.example.solfamidasback.controller.DTO;

import com.example.solfamidasback.model.DTO.UserPaidDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayFormationDTO {
    private LocalDate payDay;
    private Double inAccount;
    private Double totalPaid;
    private List<UsersPaidDTO> usersPaid;
}
