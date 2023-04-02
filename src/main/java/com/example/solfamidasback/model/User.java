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

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "password", length = 250)
    private String password ;

    @Column(name = "superadministrator")
    private Boolean superadmin;

    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private List<Material> materialList;
    //
    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private List<Absence> abesenceList;


    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private List<Login> login;

    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private List<UnsubscribeFormation> unsubscribeFormations;

    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private List<UserFormationRole> userFormationRole;
}
