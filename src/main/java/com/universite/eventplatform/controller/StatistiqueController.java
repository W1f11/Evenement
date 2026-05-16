package com.universite.eventplatform.controller;

import com.universite.eventplatform.dto.StatistiqueDTO;
import com.universite.eventplatform.service.StatistiqueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
@Tag(name = "Statistiques")
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<StatistiqueDTO> getStatistiques() {
        return ResponseEntity.ok(statistiqueService.getStatistiques());
    }
}