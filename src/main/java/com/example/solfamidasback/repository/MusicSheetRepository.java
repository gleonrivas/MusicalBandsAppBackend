package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.MusicSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicSheetRepository extends JpaRepository<MusicSheet,Integer> {

    @Query(value = "select * from music_sheet ms \n" +
            "join user_formation_role ufr on ms.id_user_formation_role = ufr.id\n" +
            "join formation f on ufr.id_formation = f.id\n" +
            "where f.id = ?", nativeQuery = true)
    List<MusicSheet> getMusicSheetByidFormation(@Param("id_formation")Integer formationId);

}
