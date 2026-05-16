package com.universite.eventplatform.service;

import com.universite.eventplatform.dto.StatistiqueDTO;
import com.universite.eventplatform.entity.Inscription;
import com.universite.eventplatform.entity.User;
import com.universite.eventplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatistiqueService {

    private final EventRepository eventRepository;
    private final InscriptionRepository inscriptionRepository;
    private final UserRepository userRepository;

    public StatistiqueDTO getStatistiques() {
        LocalDateTime now = LocalDateTime.now();
        return StatistiqueDTO.builder()
                .totalEvents(eventRepository.count())
                .eventsAVenir(eventRepository.countByDateAfter(now))
                .eventsPassés(eventRepository.countByDateBefore(now))
                .totalInscriptions(inscriptionRepository.count())
                .inscriptionsConfirmees(inscriptionRepository.countByStatut(Inscription.StatutInscription.CONFIRMEE))
                .totalUtilisateurs(userRepository.count())
                .totalEtudiants(userRepository.countByRole(User.RoleType.ETUDIANT))
                .totalOrganisateurs(userRepository.countByRole(User.RoleType.ORGANISATEUR))
                .inscriptionsParMois(buildMap(inscriptionRepository.countInscriptionsParMois()))
                .inscriptionsParTypeEvent(buildMap(inscriptionRepository.countInscriptionsParTypeEvent()))
                .build();
    }

    private Map<String, Long> buildMap(List<Object[]> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : rows) map.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        return map;
    }
}