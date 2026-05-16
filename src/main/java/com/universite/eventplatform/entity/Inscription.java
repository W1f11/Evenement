package com.universite.eventplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"etudiant_id", "event_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInscription statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @PrePersist
    protected void onCreate() {
        this.dateInscription = LocalDateTime.now();
    }

    public enum StatutInscription {
        EN_ATTENTE, CONFIRMEE, REFUSEE, ANNULEE
    }
}