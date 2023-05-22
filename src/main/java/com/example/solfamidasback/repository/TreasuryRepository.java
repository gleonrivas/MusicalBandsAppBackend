package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Treasury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TreasuryRepository extends JpaRepository<Treasury,Integer> {
    @Query(value = "select * from treasury t order by id desc limit 1 ", nativeQuery = true)
    Treasury findLastTreasury();

    @Query(value = "select * from treasury t where id_formation = ? order by id desc limit 1 ", nativeQuery = true)
    Treasury findLastTreasuryPay(@Param("id_formation")Integer formationId);
}
