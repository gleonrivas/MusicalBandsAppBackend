package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence,Integer> {
}
