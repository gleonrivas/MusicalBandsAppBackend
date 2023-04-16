package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.UserFormationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserFormationRoleRepository extends JpaRepository<UserFormationRole,Integer> {
    @Query(value = "select id_role from user_formation_role ufr" +
            " where id_formation = ? and id_user = ?", nativeQuery = true)
    List<Integer> idRolByFormationAndUser(@Param("id_formation") Integer id_formation,
                                          @Param("id_user") Integer id_user);

}
