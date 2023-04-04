package com.example.solfamidasback.model;

import com.example.solfamidasback.model.Enums.EnumMaterialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @Column(name = "material_type")
    private EnumMaterialType materialType;

    @Column(name = "active")
    private boolean active;

    @Column(name = "full_date", length = 150)
    private LocalDateTime fullDate;

    @ManyToOne
    @JoinColumn(name = "id_formation")
    @JsonIgnore
    @JsonIgnoreProperties(value = "material")
    private Formation formation;

    @JoinTable(
            name = "borrowed_material",
            joinColumns = @JoinColumn(name = "id_material", nullable = false),
            inverseJoinColumns = @JoinColumn(name="id_users", nullable = false)
    )
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Users> usersList;




}
