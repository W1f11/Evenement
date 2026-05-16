package com.universite.eventplatform.repository;

import com.universite.eventplatform.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    List<Inscription> findByEventId(Long eventId);
    List<Inscription> findByEtudiantId(Long etudiantId);
    Optional<Inscription> findByEtudiantIdAndEventId(Long etudiantId, Long eventId);
    boolean existsByEtudiantIdAndEventId(Long etudiantId, Long eventId);
    long countByEventIdAndStatut(Long eventId, Inscription.StatutInscription statut);
    long countByStatut(Inscription.StatutInscription statut);

    @Query("SELECT FUNCTION('DATE_FORMAT', i.dateInscription, '%Y-%m') as mois, COUNT(i) as total " +
            "FROM Inscription i GROUP BY mois ORDER BY mois")
    List<Object[]> countInscriptionsParMois();

    @Query("SELECT e.typeEvent as type, COUNT(i) as total " +
            "FROM Inscription i JOIN i.event e GROUP BY e.typeEvent")
    List<Object[]> countInscriptionsParTypeEvent();
}