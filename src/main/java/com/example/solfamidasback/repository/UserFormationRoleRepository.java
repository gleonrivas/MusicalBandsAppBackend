package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.DTO.RoleDTO;
import com.example.solfamidasback.model.Role;
import com.example.solfamidasback.model.UserFormationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


@Repository
public interface UserFormationRoleRepository extends JpaRepository<UserFormationRole,Integer> {
    @Query(value = "select id_role from user_formation_role ufr" +
            " where id_formation = ? and id_user = ?", nativeQuery = true)
    List<Integer> idRolByFormationAndUser(@Param("id_formation") Integer id_formation,
                                          @Param("id_user") Integer id_user);

    @Query(value = "select  r.type  from role r join user_formation_role ufr on ufr.id_role  = r.id\n" +
            "join users u  on u.id =ufr.id_user  \n" +
            "where ufr.id_formation =? and u.id =? and ufr.active =true and r.active =true", nativeQuery = true)
    Set<Integer> rolesByFormation(@Param("id_formation") Integer id_formation,
                               @Param("id") Integer id_user);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user_formation_role ufi  SET active = false where id_user = ? and id_formation = ?", nativeQuery = true)
    void deleteUserFormation(@Param("id_user")Integer userId,@Param("id_formation") Integer fomartionId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user_formation_role ufi  SET active = true where id_user = ? and id_formation = ?", nativeQuery = true)
    void reactiveUserFormation(@Param("id_user")Integer userId,@Param("id_formation") Integer fomartionId);



    @Query(value = "select id from user_formation_role ufi where id_formation = ? and id_user = ? and active = false ", nativeQuery = true)
    Integer findFormationUserFalse(@Param("id_user")Integer userId,@Param("id_formation") Integer formationId);


    @Query(value = "select id from user_formation_role ufi where id_formation = ? and id_user = ? and active = true ", nativeQuery = true)
    Integer findFormationUserTrue(@Param("id_user")Integer userId,@Param("id_formation") Integer formationId);


    boolean existsUserFormationRoleByActiveFalseAndUsersIdAndFormationId(Integer userId, Integer formationId);

    @Query(value = "select count(id_user) from user_formation_role ufr where id_formation = ? and active = true ", nativeQuery = true)
    Integer countUsersFormation(@Param("id_formation") Integer formationId);


    @Query(value = "select id_user from user_formation_role ufr where id_formation  = ?", nativeQuery = true)
    List<Integer> getAllUsersIdFormation(@Param("id_formation") Integer formationId);

    @Query(value = "select id_role from user_formation_role ufr where id_formation  = ? and id_user = ?", nativeQuery = true)
    UserFormationRole getRoleByFormantionAndUser(@Param("id_formation") Integer formationId, @Param("id_user") Integer userId);



}
