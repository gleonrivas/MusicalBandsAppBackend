package com.example.solfamidasback.model;

import com.example.solfamidasback.model.Enums.EnumRolUserFormation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "active", length = 150)
    private boolean active = true;

    @Column(name = "type")
    private EnumRolUserFormation type;


    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="role")
    @JsonIgnore
    private List<UserFormationRole> userFormationRole;

    public Role(boolean active, EnumRolUserFormation type) {
        this.active = active;
        this.type = type;
    }
}
