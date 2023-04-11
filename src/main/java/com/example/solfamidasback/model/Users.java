package com.example.solfamidasback.model;

import com.example.solfamidasback.model.Enums.EnumRolAuth;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users implements UserDetails {

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

    @Enumerated(EnumType.ORDINAL)
    private EnumRolAuth enumRolAuth;

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

    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private List<Formation> formationList;
    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private List<UserFormationInstrument> userFormationInstruments;
    @ManyToMany(mappedBy = "usersList",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="users")
    @JsonIgnore
    private Set<Material> materialList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(enumRolAuth.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
