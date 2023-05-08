package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.ExternalMusician;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalMusicianRepository extends JpaRepository<ExternalMusician,Integer> {

    public List<ExternalMusician> findAllByActiveIsTrueAndCalendar(CalendarEvent calendarEvent);

    @Query(value = "select * from external_musician where name like ? and active =true", nativeQuery = true)
    public List<ExternalMusician> getByNameAndActiveIsTrue(@Param("name")String name);

    ExternalMusician findFirstByIdAndActiveIsTrue(Integer id);
}
