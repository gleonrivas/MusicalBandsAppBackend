package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation,Integer> {

    @Query(value = "select * from formation f where id_owner = ? \n" +
            "and f.active =true", nativeQuery = true)
    List<Formation> getAllByUserAndActiveIsTrue(@Param("id_user") Integer id);

    Formation findFormationByIdAndActiveIsTrue(Integer id);
    @Query(value = "insert into user_formation_role (id_formation, id_role, id_user)" +
            " values(?,?) ", nativeQuery = true)
    void insertMiddleTable(@Param("id_user") Integer id_user,
                           @Param("id_formation") Integer id_formation);


    @Query(value = "select * from formation f order by id desc limit 1 ", nativeQuery = true)
    Formation findLastFormation();

   @Modifying
   @Query(value = "update formation f set active = false where id_owner = ? and name = ?", nativeQuery = true)
    void deleteUserFormation(@Param("userId") Integer userId, @Param("nameFormation") String nameFormation);

   @Query("select f.id from Formation f where f.users.id = :userId and f.active = false")
    Integer findFormationUserFalse(Integer userId);



}
