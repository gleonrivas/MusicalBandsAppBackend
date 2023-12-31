package com.example.solfamidasback.model;

import com.example.solfamidasback.model.Enums.EnumFormationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "formation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "designation", length = 150)
    private String designation;

    @Column(name = "type", length = 150)
    private EnumFormationType type;

    @Column(name = "foundation_year")
    private LocalDateTime fundationDate;

    @Column(name = "logo", length = 400)
    private String logo;

    @Column(name = "link", length = 400, nullable = true)
    private String link;

    @Column(name = "active")
    private Boolean active = true;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private List<UnsubscribeFormation> unsubscribeFormations;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private List<Material> materialList;


    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private List<UserFormationRole> userFormationRole;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private List<CalendarEvent> calendarEvents;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner")
    private Users users;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private List<UserFormationInstrument> userFormationInstruments;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private Set<Repertory> repertorySet;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private Set<Treasury> treasuries;


}
