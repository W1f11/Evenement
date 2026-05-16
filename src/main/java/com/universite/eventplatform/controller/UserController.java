package com.universite.eventplatform.controller;

import com.universite.eventplatform.dto.UserDTO;
import com.universite.eventplatform.entity.User;
import com.universite.eventplatform.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<List<UserDTO>> getAll() { return ResponseEntity.ok(userService.getAllUsers()); }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) { return ResponseEntity.ok(userService.getUserById(id)); }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<UserDTO> create(@RequestBody Map<String, Object> body) {
        UserDTO dto = new UserDTO();
        dto.setNom((String) body.get("nom")); dto.setEmail((String) body.get("email"));
        dto.setOrganisation((String) body.get("organisation")); dto.setDepartement((String) body.get("departement"));
        dto.setFiliere((String) body.get("filiere")); dto.setNiveau((String) body.get("niveau"));
        return ResponseEntity.ok(userService.createUser(dto, (String) body.get("password"),
                User.RoleType.valueOf((String) body.get("role"))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { userService.deleteUser(id); return ResponseEntity.noContent().build(); }

    @PatchMapping("/{id}/toggle-actif")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<UserDTO> toggleActif(@PathVariable Long id) { return ResponseEntity.ok(userService.toggleActif(id)); }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        userService.changePassword(id, body.get("oldPassword"), body.get("newPassword"));
        return ResponseEntity.ok().build();
    }
}