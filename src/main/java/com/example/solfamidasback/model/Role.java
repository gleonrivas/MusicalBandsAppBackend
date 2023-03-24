package com.example.solfamidasback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(name = "instumentalist_type", length = 150)
    private Integer fullDate;

    @Column(name = "active", length = 150)
    private boolean active;

    @Column(name = "type", length = 150)
    private Integer type;

}
