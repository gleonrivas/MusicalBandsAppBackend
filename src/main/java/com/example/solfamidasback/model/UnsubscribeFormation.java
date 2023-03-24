package com.example.solfamidasback.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "unsubscribe_formation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnsubscribeFormation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "id_user")
    private User user;

   @ManyToOne()
   @JoinColumn(name = "id_formation")
    private Formation formation;


    @JoinColumn(name = "reason")
    private String reason;

    @JoinColumn(name = "full_date")
    private LocalDate date;







}
