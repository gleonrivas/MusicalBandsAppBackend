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

    @Column(name = "nombre", length = 150)
    private String name;

    @Column(name = "designacion", length = 150)
    private String designation;
    @Column(name = "tipo", length = 150)
    private String type;

    @Column(name = "año_fundacion")
    private LocalDateTime fundationDate;

    @Column(name = "logo", length = 400)
    private String logo;

    @Column(name = "activo")
    private Boolean active;

    @OneToMany(mappedBy = "formation",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="formation")
    @JsonIgnore
    private List<Material> materialList;
}
