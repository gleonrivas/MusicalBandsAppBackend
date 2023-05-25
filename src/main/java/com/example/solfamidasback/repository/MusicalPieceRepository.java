package com.example.solfamidasback.repository;
import com.example.solfamidasback.controller.DTO.RepertoryMusicalPieceDTO;
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
            "where rmp.repertory_id =? and mp.active =true and rmp.active=true", nativeQuery = true)
    List<MusicalPiece> musicalPieceByIdRepertoireAndActiveTrue(@Param("repertory_id") Long repertoryId);
    @Query(value = "select * from musical_piece mp \n" +
            "where mp.name like ? and mp.active =true ", nativeQuery = true)
    List<MusicalPiece> getByNameLikeAndActiveTrue(String name);

    List<MusicalPiece> findAllByAuthorAndActiveIsTrue(String author);

    MusicalPiece findByIdAndActiveIsTrue(Integer id);

    MusicalPiece findFirstByActiveIsTrueOrderByIdDesc();

    @Modifying
    @Query(value = "insert into repertory_musical_piece (repertory_id, musical_piece_id, active) values (?,?,?)", nativeQuery = true)
    @Transactional
    void createRelationRepertoryMudicalPiece(@Param("repertory_id")Integer repertoryId,
                                             @Param("musical_piece_id")Integer musicalPieceId,
                                             @Param("active")Boolean active);

    @Modifying
    @Query(value = "select * from repertory_musical_piece rmp \n" +
            "where repertory_id = ? and musical_piece_id = ? and rmp.active = ? ", nativeQuery = true)
    @Transactional
    RepertoryMusicalPieceDTO getRepertoryMusicalPiece(@Param("repertory_id")Integer repertoryId,
                                                       @Param("musical_piece_id")Integer musicalPieceId,
                                                       @Param("active")Boolean active);

    @Modifying
    @Query(value = " UPDATE repertory_musical_piece\n" +
            "    SET active  = false\n" +
            "    WHERE repertory_id  = ? and musical_piece_id = ? ", nativeQuery = true)
    @Transactional
    void updateRelation(@Param("repertory_id")Integer repertoryId,
                        @Param("musical_piece_id")Integer musicalPieceId);

}
