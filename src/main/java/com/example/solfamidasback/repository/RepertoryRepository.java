package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Repertory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RepertoryRepository extends JpaRepository<Repertory,Integer> {

    Set<Repertory> findAllByFormationAndActiveIsTrue(Formation formation);

}
