package com.example.solfamidasback.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
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

    @Column(name = "superadministrador")
    private Boolean superadmin;


}
