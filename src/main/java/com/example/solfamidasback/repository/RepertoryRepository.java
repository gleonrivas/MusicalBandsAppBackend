package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Repertory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepertoryRepository extends JpaRepository<Repertory,Integer> {

}
