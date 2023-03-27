package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findAllByActiveIsTrue();

    User findAllByNameAndActiveIsTrue(String name);

    boolean findUserByEmail(String email);


}
