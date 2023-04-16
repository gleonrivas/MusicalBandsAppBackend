package com.example.solfamidasback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "musical_piece")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MusicalPiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "author", length = 150)
    private String author;

    @Column(name = "length")
    private Double length;
    @Column(name = "active")
    private boolean active = true;

    @ManyToMany(mappedBy = "musicalPieceSet")
    Set<Repertory> repertorySet;


}
