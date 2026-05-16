package com.universite.eventplatform.controller;

import com.universite.eventplatform.dto.InscriptionDTO;
import com.universite.eventplatform.service.InscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
@Tag(name = "Inscriptions")
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @PostMapping("/inscrire")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<InscriptionDTO> inscrire(@RequestParam Long etudiantId, @RequestParam Long eventId) {
        return ResponseEntity.ok(inscriptionService.inscrire(etudiantId, eventId));
    }

    @DeleteMapping("/annuler")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<Void> annuler(@RequestParam Long etudiantId, @RequestParam Long eventId) {
        inscriptionService.annuler(etudiantId, eventId); return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<InscriptionDTO> valider(@PathVariable Long id) { return ResponseEntity.ok(inscriptionService.valider(id)); }

    @PatchMapping("/{id}/refuser")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<InscriptionDTO> refuser(@PathVariable Long id) { return ResponseEntity.ok(inscriptionService.refuser(id)); }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<List<InscriptionDTO>> getByEvent(@PathVariable Long eventId) { return ResponseEntity.ok(inscriptionService.getByEvent(eventId)); }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<InscriptionDTO>> getByEtudiant(@PathVariable Long etudiantId) { return ResponseEntity.ok(inscriptionService.getByEtudiant(etudiantId)); }
}