package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_formation_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFormationRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Users users;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formation")
    private Formation formation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role")
    private Role role;

    @OneToMany(mappedBy = "userFormationRole",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="user_formation_role")
    @JsonIgnore
    private Set<MusicSheet> musicSheets;

    @Column(name = "active")
    private boolean active;

    public UserFormationRole(Users users, Formation formation, Role role) {
        this.users = users;
        this.formation = formation;
        this.role = role;
    }
}
