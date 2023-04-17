package com.example.solfamidasback.repository;
import com.example.solfamidasback.model.MusicalPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MusicalPieceRepository extends JpaRepository<MusicalPiece,Integer> {

    List<MusicalPiece> findAllByActiveIsTrue();

    @Query(value = "select mp.* from musical_piece mp \n" +
            "join repertoire_musical_piece rmp \n" +
            "on rmp.musical_piece_id =mp.id \n" +
            "where rmp.repertoire_id =? and mp.active =true ", nativeQuery = true)
    List<MusicalPiece> musicalPieceByIdRepertoireAndActiveTrue(@Param("repertoire_id") Long repertoireId);
    @Query(value = "select mp.* from musical_piece mp \n" +
            "where mp.name like ? and mp.active =true ", nativeQuery = true)
    List<MusicalPiece> getByNameLikeAndActiveTrue(String name);

}
