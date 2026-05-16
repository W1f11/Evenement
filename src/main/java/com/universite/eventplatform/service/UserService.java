package com.universite.eventplatform.service;

import com.universite.eventplatform.dto.UserDTO;
import com.universite.eventplatform.entity.*;
import com.universite.eventplatform.exception.InvalidRequestException;
import com.universite.eventplatform.exception.ResourceNotFoundException;
import com.universite.eventplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return toDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + id)));
    }

    public UserDTO createUser(UserDTO dto, String password, User.RoleType role) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new InvalidRequestException("Email déjà utilisé.");
        return toDTO(userRepository.save(buildUserFromRole(role, dto, password)));
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + id));
        user.setNom(dto.getNom());
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail()))
            throw new InvalidRequestException("Email déjà utilisé.");
        user.setEmail(dto.getEmail());
        if (user instanceof Etudiant e) { e.setFiliere(dto.getFiliere()); e.setNiveau(dto.getNiveau()); }
        else if (user instanceof Organisateur o) { o.setOrganisation(dto.getOrganisation()); }
        else if (user instanceof Administrateur a) { a.setDepartement(dto.getDepartement()); }
        return toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("Utilisateur non trouvé: " + id);
        userRepository.deleteById(id);
    }

    public UserDTO toggleActif(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + id));
        user.setActif(!user.isActif());
        return toDTO(userRepository.save(user));
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + id));
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new InvalidRequestException("Ancien mot de passe incorrect.");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = UserDTO.builder()
                .id(user.getId()).nom(user.getNom()).email(user.getEmail())
                .role(user.getRole()).actif(user.isActif()).build();
        if (user instanceof Etudiant e) { dto.setFiliere(e.getFiliere()); dto.setNiveau(e.getNiveau()); }
        else if (user instanceof Organisateur o) { dto.setOrganisation(o.getOrganisation()); }
        else if (user instanceof Administrateur a) { dto.setDepartement(a.getDepartement()); }
        return dto;
    }

    private User buildUserFromRole(User.RoleType role, UserDTO dto, String rawPassword) {
        String encoded = passwordEncoder.encode(rawPassword);
        return switch (role) {
            case ETUDIANT -> { Etudiant e = new Etudiant(); e.setNom(dto.getNom()); e.setEmail(dto.getEmail());
                e.setPassword(encoded); e.setFiliere(dto.getFiliere()); e.setNiveau(dto.getNiveau()); e.setActif(true); yield e; }
            case ORGANISATEUR -> { Organisateur o = new Organisateur(); o.setNom(dto.getNom()); o.setEmail(dto.getEmail());
                o.setPassword(encoded); o.setOrganisation(dto.getOrganisation()); o.setActif(true); yield o; }
            case ADMINISTRATEUR -> { Administrateur a = new Administrateur(); a.setNom(dto.getNom()); a.setEmail(dto.getEmail());
                a.setPassword(encoded); a.setDepartement(dto.getDepartement()); a.setActif(true); yield a; }
        };
    }
}