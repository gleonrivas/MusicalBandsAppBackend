package com.example.solfamidasback.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "Calendar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Integer id;

    @JoinColumn(name = "place")
    private String place;

    @JoinColumn(name = "full_date")
    private LocalDate date;

    @JoinColumn(name = "title")
    private String title;

    @JoinColumn(name = "description")
    private String description;

    @JoinColumn(name = "type")
    private String type;

    @JoinColumn(name = "paid")
    private boolean paid;

    @JoinColumn(name = "amount")
    private Double amount;

    @JoinColumn(name = "consideration_rehearsal")
    private Double considerationRehearsal;

    @JoinColumn(name = "consideration_bolus")
    private Double considerationBolus;

    //    @ManyToOne()
//    @JoinColumn(name = "id_formation")
//    private Formation;


//    @OneToMany(mappedBy = "calendar",fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value="calendar")
//    @JsonIgnore
//    private List<ExternalMusician> extenalMusicianList;
}
