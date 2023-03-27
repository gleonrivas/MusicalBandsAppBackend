package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", length = 150)
    private String name;

    @Column(name = "apellidos", length = 150)
    private String surName;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "fecha_nacimiento")
    private LocalDateTime birthDate;

    @Column(name = "dni", length = 15)
    private String dni;

    @Column(name = "active", length = 15)
    private Boolean active = true;

    @Column(name = "superadministrador")
    private Boolean superadmin;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<Material> materialList;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<Absence> abeenceList;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<MusicSheet> musicSheets;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<Login> login;
}
