package com.example.solfamidasback.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "cupon")
@Getter
@Setter
@AllArgsConstructor
public class Login {

}
