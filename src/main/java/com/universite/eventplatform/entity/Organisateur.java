package com.universite.eventplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "organisateurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class Organisateur extends User{

    private String organisation;
    @OneToMany(mappedBy = "organisateur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> envents;

}
