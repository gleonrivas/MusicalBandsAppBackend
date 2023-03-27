package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Material;
import com.example.solfamidasback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation,Integer> {

    @Query(value = "select * from formation f join music_sheet ms \n" +
            "on ms.id_formation = f.id where ms.id_user = ? and f.activo =true ", nativeQuery = true)
    List<Formation> findAllByUserAndActiveIsTrue(@Param("id_user") Integer id);
}
