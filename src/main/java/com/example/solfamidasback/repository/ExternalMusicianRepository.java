package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.ExternalMusician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalMusicianRepository extends JpaRepository<ExternalMusician,Integer> {
}
