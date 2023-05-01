package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation,Integer> {

    @Query(value = "select * from formation f where id_owner = ? \n" +
            "and f.active =true", nativeQuery = true)
    List<Formation> getAllByUserOwnerAndActiveIsTrue(@Param("id_user") Integer id);

    Formation findFormationByIdAndActiveIsTrue(Integer id);

    @Query(value = "insert into user_formation_role (id_formation, id_role, id_user)" +
            " values(?,?) ", nativeQuery = true)
    void insertMiddleTable(@Param("id_user") Integer id_user,
                           @Param("id_formation") Integer id_formation);


    @Query(value = "select * from formation f order by id desc limit 1 ", nativeQuery = true)
    Formation findLastFormation();

    @Query(value = "select f.* from formation f join user_formation_role ufr\n" +
            "on f.id = ufr.id_formation \n" +
            "join users u on u.id =ufr.id_user where u.id = ? and f.active =true", nativeQuery = true)
    List<Formation> getAllByUserAndActiveIsTrue(Integer id);

    @Query(value = "select * from formation where name like CONCAT('%', ?,'%');", nativeQuery = true)
    List<Formation> findFormationsByLike(String nameFormation);






}
