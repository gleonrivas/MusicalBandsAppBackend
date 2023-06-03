package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Treasury;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreasuryRepository extends JpaRepository<Treasury,Integer> {
    @Query(value = "select * from treasury t order by id desc limit 1 ", nativeQuery = true)
    Treasury findLastTreasury();

    @Query(value = "select * from treasury t where id_formation = ? order by id desc limit 1 ", nativeQuery = true)
    Treasury findLastTreasuryPay(@Param("id_formation")Integer formationId);

    @Query(value = "select * from treasury t where id_formation = ? order by id asc ", nativeQuery = true)
    List<Treasury> getAllTreasuryFormation(@Param("id_formation")Integer formationId);

    @EntityGraph(attributePaths = "formation")
    Optional<Treasury> findByIdAndActiveIsTrue(Integer id);

    Treasury findFirstByFormationIdAndActiveOrderByReceiveMoneyDateDesc(Integer formationId, boolean active);


}
