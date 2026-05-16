package com.universite.eventplatform.dto;

import com.universite.eventplatform.entity.User;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nom;
    private String email;
    private User.RoleType role;
}
