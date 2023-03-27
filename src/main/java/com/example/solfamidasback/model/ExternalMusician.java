package com.example.solfamidasback.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "external_musician")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalMusician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_calendar")
    @JsonIgnore
    @JsonIgnoreProperties(value = "external_musician")
    private CalendarEvent calendar;


    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "surname", length = 150)
    private String surname;

    @Column(name = "dni", length = 150)
    private String dni;

    @Column(name = "amount", length = 150)
    private Integer amount;

    @Column(name = "active", length = 150)
    private boolean active;

}
