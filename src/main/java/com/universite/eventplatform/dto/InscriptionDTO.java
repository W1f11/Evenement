package com.universite.eventplatform.dto;

import com.universite.eventplatform.entity.Inscription;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InscriptionDTO {
    private Long id;
    private LocalDateTime dateInscription;
    private Inscription.StatutInscription statut;
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantEmail;
    private Long eventId;
    private String eventTitre;
}