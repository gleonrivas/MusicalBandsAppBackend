package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Bool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "formacion")
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
    private String type;

    @Column(name = "foundation_year")
    private LocalDateTime fundationDate;

    @Column(name = "logo", length = 400)
    private String logo;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private List<Material> materialList;

    //va con la tabla triple relacionada one to many



}