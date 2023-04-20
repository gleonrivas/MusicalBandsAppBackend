package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    List<Users> findAllByActiveIsTrue();

    Users findAllByNameAndActiveIsTrue(String name);

   Optional<Users> findByEmail(String email);

   Users findByEmailAndActiveIsTrue(String email);

    Users findByIdAndActiveIsTrue(Integer id);

  boolean existsByEmail(String email);



}