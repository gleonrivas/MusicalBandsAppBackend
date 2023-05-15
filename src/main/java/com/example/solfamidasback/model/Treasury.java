package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "treasury")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Treasury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_receive_money")
    private LocalDate receiveMoneyDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formation")
    private Formation formation;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "active")
    private boolean active = true;

}
