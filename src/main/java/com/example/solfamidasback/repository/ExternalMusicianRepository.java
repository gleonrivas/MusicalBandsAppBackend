package com.example.solfamidasback.repository;

import com.example.solfamidasback.model.ExternalMusician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalMusicianRepository extends JpaRepository<ExternalMusician,Integer> {

    public List<ExternalMusician> findAllByActiveIsTrueAndCalendar(Integer idCalendar);

    public List<ExternalMusician> findAllByNameAndActiveIsTrue(String name);

    List<ExternalMusician> findAllByCalendarId(Integer calendar);

}
