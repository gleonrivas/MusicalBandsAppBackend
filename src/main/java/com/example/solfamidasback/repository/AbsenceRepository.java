package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence,Integer> {
    @Query(value = "select * absence where id_user = ? ", nativeQuery = true)
    List<Absence> getAllByIdUser(@Param("id_user")Integer userId);

}
