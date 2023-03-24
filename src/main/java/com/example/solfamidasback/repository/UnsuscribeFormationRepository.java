package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Material;
import com.example.solfamidasback.model.UnsubscribeFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnsuscribeFormationRepository extends JpaRepository<UnsubscribeFormation,Integer> {
}
