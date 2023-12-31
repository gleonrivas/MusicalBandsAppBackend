package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "repertory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Repertory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active")
    private boolean active = true;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formation")
    private Formation formation;

    @ManyToMany
    @JoinTable(
            name = "repertory_musical_piece",
            joinColumns = @JoinColumn(name = "repertory_id"),
            inverseJoinColumns = @JoinColumn(name = "musical_piece_id"))
    Set<MusicalPiece> musicalPieceSet;

    @OneToMany(mappedBy = "repertory",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="repertory")
    @JsonIgnore
    private Set<CalendarEvent> calendarEvents;



}
