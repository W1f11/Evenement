package com.universite.eventplatform.dto;

import lombok.*;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatistiqueDTO {
    private long totalEvents;
    private long eventsAVenir;
    private long eventsPassés;
    private long totalInscriptions;
    private long inscriptionsConfirmees;
    private long totalUtilisateurs;
    private long totalEtudiants;
    private long totalOrganisateurs;
    private Map<String, Long> inscriptionsParMois;
    private Map<String, Long> inscriptionsParTypeEvent;
}