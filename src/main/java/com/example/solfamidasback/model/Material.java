package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "material")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "transferred_material", length = 150)
    private String transferredMaterial;

    @Column(name = "active")
    private boolean active;

    @Column(name = "full_date", length = 150)
    private LocalDateTime fullDate;

    @ManyToOne
    @JoinColumn(name = "id_formation")
    @JsonIgnore
    @JsonIgnoreProperties(value = "material")
    private Formation formation;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    @JsonIgnoreProperties(value = "material")
    private User user;



}
