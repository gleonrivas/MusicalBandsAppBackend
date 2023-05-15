package com.example.solfamidasback.repository;
import com.example.solfamidasback.controller.DTO.BorrowedMaterialDTO;
import com.example.solfamidasback.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material,Integer> {

    Material findByIdAndActiveIsTrue(Integer id);

    @Modifying
    @Query(value = "insert into borrowed_material (id_material, id_users) values (?,?)", nativeQuery = true)
    @Transactional
    void createBorrowedMaterial(@Param("id_material")Integer materialId,@Param("id_user")Integer userId );

    @Query(value = "select id_material, id_users from borrowed_material where id_material = ? and id_users = ?;", nativeQuery = true)
    List<Integer> findBorrowedMaterial(@Param("id_material")Integer materialId, @Param("id_user")Integer userId );

    @Transactional
    @Modifying
    @Query(value = "update borrowed_material set id_users = ? where id_material = ? and id_users = ?", nativeQuery = true)
    void changeUser(@Param("id_users")Integer userNewId,@Param("id_material")Integer materialId, @Param("id_users")Integer userId );

    @Transactional
    @Modifying
    @Query(value = "UPDATE borrowed_material SET id_material = ? where id_users = ? and id_material = ?", nativeQuery = true)
    void changeMaterial(@Param("id_material")Integer materialNewId,@Param("id_users")Integer userId, @Param("id_material")Integer materialId );

    @Transactional
    @Modifying
    @Query(value = "delete from borrowed_material where id_material = ? and id_users = ?", nativeQuery = true)
    void deleteBorrowedMaterial(@Param("id_material")Integer materialId,@Param("id_users")Integer userId );


    @Query(value = "select * from material m where id_formation = ? and active = true", nativeQuery = true)
    List<Material> getAllByIdFormation(@Param("id_formation")Integer formationID);

}
