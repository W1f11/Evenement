package com.universite.eventplatform.service;

import com.universite.eventplatform.dto.AuthResponseDTO;
import com.universite.eventplatform.dto.LoginRequestDTO;
import com.universite.eventplatform.dto.UserDTO;
import com.universite.eventplatform.entity.*;
import com.universite.eventplatform.exception.InvalidRequestException;
import com.universite.eventplatform.repository.UserRepository;
import com.universite.eventplatform.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidRequestException("Utilisateur introuvable"));

        if (!user.isActif()) {
            throw new InvalidRequestException("Compte désactivé. Veuillez contacter l'administrateur.");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .nom(user.getNom())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserDTO register(UserDTO userDTO, String password) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new InvalidRequestException("Un compte avec cet email existe déjà.");
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setNom(userDTO.getNom());
        etudiant.setEmail(userDTO.getEmail());
        etudiant.setPassword(passwordEncoder.encode(password));
        etudiant.setFiliere(userDTO.getFiliere());
        etudiant.setNiveau(userDTO.getNiveau());
        etudiant.setActif(true);

        Etudiant saved = (Etudiant) userRepository.save(etudiant);

        return UserDTO.builder()
                .id(saved.getId())
                .nom(saved.getNom())
                .email(saved.getEmail())
                .role(User.RoleType.ETUDIANT)
                .actif(saved.isActif())
                .filiere(saved.getFiliere())
                .niveau(saved.getNiveau())
                .build();
    }
}