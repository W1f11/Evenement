package com.universite.eventplatform.dto;

import com.universite.eventplatform.entity.TypeEvent;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EventDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime date;
    private String lieu;
    private int capacite;
    private TypeEvent typeEvent;
    private boolean validationManuelle;
    private Long organisateurId;
    private String organisateurNom;
    private int placesRestantes;
    private int nombreInscrits;
    private LocalDateTime createdAt;
}