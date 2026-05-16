package com.universite.eventplatform.repository;

import com.universite.eventplatform.entity.Event;
import com.universite.eventplatform.entity.TypeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganisateurId(Long organisateurId);
    List<Event> findByDateAfterOrderByDateAsc(LocalDateTime date);
    List<Event> findByDateBeforeOrderByDateDesc(LocalDateTime date);

    @Query("SELECT e FROM Event e WHERE " +
            "(:keyword IS NULL OR LOWER(e.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:lieu IS NULL OR LOWER(e.lieu) LIKE LOWER(CONCAT('%', :lieu, '%'))) " +
            "AND (:typeEvent IS NULL OR e.typeEvent = :typeEvent)")
    List<Event> search(@Param("keyword") String keyword,
                       @Param("lieu") String lieu,
                       @Param("typeEvent") TypeEvent typeEvent);

    long countByDateAfter(LocalDateTime date);
    long countByDateBefore(LocalDateTime date);
}