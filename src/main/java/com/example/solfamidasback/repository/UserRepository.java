package com.example.solfamidasback.repository;

import com.example.solfamidasback.controller.DTO.UsersFormationRoleDTO;
import com.example.solfamidasback.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    List<Users> findAllByActiveTrue();

    Users findAllByNameAndActiveIsTrue(String name);

   Optional<Users> findByEmail(String email);


    Users findByEmailAndActiveTrue(String email);
    Users findByEmailAndActiveTrueAndSuperadminTrue(String email);

    Users findByIdAndActiveIsTrue(Integer id);

    boolean existsByEmail(String email);

    @Query(value = "select u.*  from users u join user_formation_role ufr on ufr.id_user = u.id\n" +
            "where ufr.id_formation =? and ufr.active =true ", nativeQuery = true)
    List<Users> getUsersByFormation(@Param("id_formation") Integer formationId);


}