package com.universite.eventplatform.dto;
import com.universite.eventplatform.entity.User;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {

    private Long id;
    private String nom;
    private String email;
    private User.RoleType role;
    private boolean actif;
    private String filiere;
    private String niveau;
    private String organisation;
    private String departement;

}
