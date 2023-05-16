package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "calendar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Integer id;

    @JoinColumn(name = "place")
    private String place;

    @JoinColumn(name = "full_date")
    private LocalDate date;

    @JoinColumn(name = "title")
    private String title;

    @JoinColumn(name = "description")
    private String description;

    @JoinColumn(name = "type")
    private String type;

    @JoinColumn(name = "paid")
    private boolean paid;

    @JoinColumn(name = "amount")
    private Double amount;

    @JoinColumn(name = "penalty_ponderation")
    private Double penaltyPonderation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formation")
    private Formation formation;


    @OneToMany(mappedBy = "calendar",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="calendar")
    @JsonIgnore
    private List<ExternalMusician> extenalMusicianList;

    @OneToMany(mappedBy = "calendar",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="calendar")
    @JsonIgnore
    private List<Absence> absenceList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_repertory")
    @JsonIgnore
    @JsonIgnoreProperties(value = "repertory")
    private Repertory repertory;
}
