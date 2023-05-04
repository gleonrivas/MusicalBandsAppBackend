package com.example.solfamidasback.repository;
import com.example.solfamidasback.model.MusicalPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MusicalPieceRepository extends JpaRepository<MusicalPiece,Integer> {

    List<MusicalPiece> findAllByActiveIsTrue();

    @Query(value = "select mp.* from musical_piece mp \n" +
            "join repertory_musical_piece rmp \n" +
            "on rmp.musical_piece_id = mp.id \n" +
            "where rmp.repertory_id =? and mp.active =true ", nativeQuery = true)
    List<MusicalPiece> musicalPieceByIdRepertoireAndActiveTrue(@Param("repertory_id") Long repertoryId);
    @Query(value = "select * from musical_piece mp \n" +
            "where mp.name like ? and mp.active =true ", nativeQuery = true)
    List<MusicalPiece> getByNameLikeAndActiveTrue(String name);

    List<MusicalPiece> findAllByAuthorAndActiveIsTrue(String author);

    MusicalPiece findByIdAndActiveIsTrue(Long id);

    MusicalPiece findFirstByActiveIsTrueOrderByIdDesc();

    @Modifying
    @Query(value = "insert into repertory_musical_piece (repertory_id, musical_piece_id) values (?,?)", nativeQuery = true)
    @Transactional
    void createRelationRepertoryMudicalPiece(@Param("repertory_id")Integer repertoryId,@Param("musical_piece_id")Integer musicalPieceId );

}
