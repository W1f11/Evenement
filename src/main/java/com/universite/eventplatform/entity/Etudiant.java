package com.universite.eventplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "etudiants")
@DiscriminatorValue("ETUDIANT")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Etudiant extends User{

    private String filiere;
    private String niveau;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscription> inscriptions;

}
