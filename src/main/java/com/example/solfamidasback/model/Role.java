package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Column(name = "instumentalist_type", length = 150)
    private Integer fullDate;

    @Column(name = "active", length = 150)
    private boolean active;

    @Column(name = "type", length = 150)
    private Integer type;

    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value="role")
    @JsonIgnore
    private List<MusicSheet> musicSheets;

}
