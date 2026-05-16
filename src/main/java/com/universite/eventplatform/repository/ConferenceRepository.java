package com.universite.eventplatform.repository;

import com.universite.eventplatform.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByEventId(Long eventId);
    List<Conference> findByEventIsNull();
}