package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Treasury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent,Integer> {

    CalendarEvent findFirstById(Integer id);

    CalendarEvent findCalendarEventById(Integer id);

    @Query(value = "select * from calendar c where id_formation = ?", nativeQuery = true)
    List<CalendarEvent> getCalendarFormation(@Param("id_formation")Integer formationId);

}
