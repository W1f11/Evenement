package com.universite.eventplatform.controller;

import com.universite.eventplatform.dto.*;
import com.universite.eventplatform.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody Map<String, Object> body) {
        UserDTO dto = new UserDTO();
        dto.setNom((String) body.get("nom"));
        dto.setEmail((String) body.get("email"));
        dto.setFiliere((String) body.get("filiere"));
        dto.setNiveau((String) body.get("niveau"));
        return ResponseEntity.ok(authService.register(dto, (String) body.get("password")));
    }
}