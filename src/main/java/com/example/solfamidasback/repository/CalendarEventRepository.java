package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent,Integer> {

    CalendarEvent findFirstById(Integer id);

    CalendarEvent findCalendarEventById(Integer id);

}
