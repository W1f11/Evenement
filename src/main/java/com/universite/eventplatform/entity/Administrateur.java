package com.universite.eventplatform.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "administrateurs")
@DiscriminatorValue("ADMINISTRATEUR")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Administrateur extends User{
    private String departement;

}
