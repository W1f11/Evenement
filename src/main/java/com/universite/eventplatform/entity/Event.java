package com.universite.eventplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private int capacite;

    @Enumerated(EnumType.STRING)
    private TypeEvent typeEvent;

    private boolean validationManuelle = false;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id")
    private Organisateur organisateur;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conference> conferences;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public int getPlacesRestantes() {
        if (inscriptions == null) return capacite;
        long confirmed = inscriptions.stream()
                .filter(i -> i.getStatut() == Inscription.StatutInscription.CONFIRMEE)
                .count();
        return capacite - (int) confirmed;
    }
}