package com.universite.eventplatform.controller;

import com.universite.eventplatform.dto.EventDTO;
import com.universite.eventplatform.entity.TypeEvent;
import com.universite.eventplatform.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Événements")
public class EventController {

    private final EventService eventService;

    @GetMapping public ResponseEntity<List<EventDTO>> getAll() { return ResponseEntity.ok(eventService.getAllEvents()); }
    @GetMapping("/{id}") public ResponseEntity<EventDTO> getById(@PathVariable Long id) { return ResponseEntity.ok(eventService.getEventById(id)); }
    @GetMapping("/a-venir") public ResponseEntity<List<EventDTO>> getAVenir() { return ResponseEntity.ok(eventService.getEventsAVenir()); }
    @GetMapping("/passes") public ResponseEntity<List<EventDTO>> getPassés() { return ResponseEntity.ok(eventService.getEventsPassés()); }
    @GetMapping("/organisateur/{id}") public ResponseEntity<List<EventDTO>> getByOrg(@PathVariable Long id) { return ResponseEntity.ok(eventService.getEventsByOrganisateur(id)); }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> search(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) String lieu,
                                                 @RequestParam(required = false) TypeEvent typeEvent) {
        return ResponseEntity.ok(eventService.searchEvents(keyword, lieu, typeEvent));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO dto, @RequestParam Long organisateurId) {
        return ResponseEntity.ok(eventService.createEvent(dto, organisateurId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody EventDTO dto) { return ResponseEntity.ok(eventService.updateEvent(id, dto)); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'ORGANISATEUR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { eventService.deleteEvent(id); return ResponseEntity.noContent().build(); }
}