package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "surname", length = 150)
    private String surName;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "birthdate")
    private LocalDateTime birthDate;

    @Column(name = "dni", length = 15)
    private String dni;

    @Column(name = "superadministrator")
    private Boolean superadmin;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "password", length = 400)
    private String password;



    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<Material> materialList;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<Absence> abesenceList;



    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<Login> login;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user")
    @JsonIgnore
    private List<UserFormationRole> userFormationRole;
}
