package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.CalendarEvent;
import com.example.solfamidasback.model.Formation;
import com.example.solfamidasback.model.Repertory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RepertoryRepository extends JpaRepository<Repertory,Integer> {

    Set<Repertory> findAllByFormationAndActiveIsTrue(Formation formation);

    Set<Repertory> findAllByActiveIsTrue();

    Repertory findByIdAndActiveIsTrue(Integer id);

    @Query(value = "select mp.* from repertory mp join calendar c on c.id_repertory = mp.id where c.id=? and mp.active =true", nativeQuery = true)
    Repertory getRepertoryByIdCalendar(@Param("calendar_event_id")Integer CalendarId);

}
