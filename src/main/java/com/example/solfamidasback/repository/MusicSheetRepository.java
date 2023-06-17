package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.MusicSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicSheetRepository extends JpaRepository<MusicSheet,Integer> {

    @Query(value = "select ms.* from music_sheet ms \n" +
            "join user_formation_role ufr on ms.id_user_formation_role = ufr.id\n" +
            "join formation f on ufr.id_formation = f.id\n" +
            "join users u on ufr.id_user = u.id\n" +
            "where f.id = ?\n" +
            "and u.id = ?", nativeQuery = true)
    List<MusicSheet> getMusicSheetByidFormationAndUser(@Param("id_formation")Integer formationId, @Param("id_user")Integer idUser);

    MusicSheet findTopById(Integer id);

}
