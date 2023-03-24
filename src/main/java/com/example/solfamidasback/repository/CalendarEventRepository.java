package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarEventRepository extends JpaRepository<Calendar,Integer> {
}
