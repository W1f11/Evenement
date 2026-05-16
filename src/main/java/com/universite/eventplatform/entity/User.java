package com.universite.eventplatform.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType =  DiscriminatorType.STRING)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column (nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    private RoleType role;

    private boolean actif = true;
    public enum RoleType {
        ADMINISTRATEUR, ORGANISATEUR, ETUDIANT
    }



}
