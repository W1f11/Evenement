package com.universite.eventplatform.service;

import com.universite.eventplatform.dto.EventDTO;
import com.universite.eventplatform.entity.*;
import com.universite.eventplatform.exception.ResourceNotFoundException;
import com.universite.eventplatform.repository.EventRepository;
import com.universite.eventplatform.repository.InscriptionRepository;
import com.universite.eventplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final InscriptionRepository inscriptionRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        return toDTO(eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé: " + id)));
    }

    public List<EventDTO> getEventsAVenir() {
        return eventRepository.findByDateAfterOrderByDateAsc(LocalDateTime.now())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EventDTO> getEventsPassés() {
        return eventRepository.findByDateBeforeOrderByDateDesc(LocalDateTime.now())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByOrganisateur(Long id) {
        return eventRepository.findByOrganisateurId(id).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EventDTO> searchEvents(String keyword, String lieu, TypeEvent typeEvent) {
        return eventRepository.search(keyword, lieu, typeEvent).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EventDTO createEvent(EventDTO dto, Long organisateurId) {
        Organisateur organisateur = (Organisateur) userRepository.findById(organisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Organisateur non trouvé: " + organisateurId));
        Event event = new Event();
        mapDtoToEntity(dto, event);
        event.setOrganisateur(organisateur);
        return toDTO(eventRepository.save(event));
    }

    public EventDTO updateEvent(Long id, EventDTO dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé: " + id));
        mapDtoToEntity(dto, event);
        return toDTO(eventRepository.save(event));
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id))
            throw new ResourceNotFoundException("Événement non trouvé: " + id);
        eventRepository.deleteById(id);
    }

    private void mapDtoToEntity(EventDTO dto, Event event) {
        event.setTitre(dto.getTitre()); event.setDescription(dto.getDescription());
        event.setDate(dto.getDate()); event.setLieu(dto.getLieu());
        event.setCapacite(dto.getCapacite()); event.setTypeEvent(dto.getTypeEvent());
        event.setValidationManuelle(dto.isValidationManuelle());
    }

    public EventDTO toDTO(Event event) {
        long confirmed = inscriptionRepository.countByEventIdAndStatut(
                event.getId(), Inscription.StatutInscription.CONFIRMEE);
        return EventDTO.builder()
                .id(event.getId()).titre(event.getTitre()).description(event.getDescription())
                .date(event.getDate()).lieu(event.getLieu()).capacite(event.getCapacite())
                .typeEvent(event.getTypeEvent()).validationManuelle(event.isValidationManuelle())
                .organisateurId(event.getOrganisateur() != null ? event.getOrganisateur().getId() : null)
                .organisateurNom(event.getOrganisateur() != null ? event.getOrganisateur().getNom() : null)
                .nombreInscrits((int) confirmed).placesRestantes(event.getCapacite() - (int) confirmed)
                .createdAt(event.getCreatedAt()).build();
    }
}