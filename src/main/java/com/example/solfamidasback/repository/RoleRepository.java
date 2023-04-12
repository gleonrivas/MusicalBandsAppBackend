package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    @Query(value = "select * from role order by id desc limit 1 ", nativeQuery = true)
    Role findFirstOrderByIdDesc();


}
