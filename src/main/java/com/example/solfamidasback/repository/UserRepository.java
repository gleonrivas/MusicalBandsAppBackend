package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    List<User> findAllByActiveIsTrue();

    List<User> findAllByNameAndActiveIsTrue(String name);

    User findUserByEmailAndActiveIsTrue(String email);


}
