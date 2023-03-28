package com.example.solfamidasback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "music_sheet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MusicSheet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10)
    private Integer id;


    @Column(name = "ms_pdf", nullable = false, length = 150)
    private String musicSheetPdf;

    @Column(name = "instrument_type", nullable = false)
    private Integer instrumentType;

    @ManyToOne
    @JoinColumn(name = "id_user_formation_role")
    @JsonIgnore
    @JsonIgnoreProperties(value = "music_sheet")
    private UserFormationRole userFormationRole;
}
