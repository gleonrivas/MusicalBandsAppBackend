package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    Users findAllByActiveIsTrue();

    Users findAllByNameAndActiveIsTrue(String name);

    boolean findUserByEmail(String email);

    boolean findByIdAndActiveIsTrue(Integer id);


}