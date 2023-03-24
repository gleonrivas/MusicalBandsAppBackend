package com.example.solfamidasback.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "absence")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "full_date", length = 150)
    private LocalDateTime fullDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    @JsonIgnoreProperties(value = "absence")
    private User user;



    @ManyToOne
    @JoinColumn(name = "id_calendar")
    @JsonIgnore
    @JsonIgnoreProperties(value = "absence")
    private CalendarEvent calendar;








}
