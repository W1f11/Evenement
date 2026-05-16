package com.universite.eventplatform.service;

import com.universite.eventplatform.dto.InscriptionDTO;
import com.universite.eventplatform.entity.*;
import com.universite.eventplatform.exception.InvalidRequestException;
import com.universite.eventplatform.exception.ResourceNotFoundException;
import com.universite.eventplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public InscriptionDTO inscrire(Long etudiantId, Long eventId) {
        if (inscriptionRepository.existsByEtudiantIdAndEventId(etudiantId, eventId))
            throw new InvalidRequestException("Vous êtes déjà inscrit à cet événement.");

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé: " + eventId));

        long confirmed = inscriptionRepository.countByEventIdAndStatut(eventId, Inscription.StatutInscription.CONFIRMEE);
        if (confirmed >= event.getCapacite())
            throw new InvalidRequestException("Cet événement est complet.");

        Etudiant etudiant = (Etudiant) userRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé: " + etudiantId));

        Inscription inscription = new Inscription();
        inscription.setEtudiant(etudiant);
        inscription.setEvent(event);
        inscription.setStatut(event.isValidationManuelle()
                ? Inscription.StatutInscription.EN_ATTENTE
                : Inscription.StatutInscription.CONFIRMEE);

        return toDTO(inscriptionRepository.save(inscription));
    }

    public void annuler(Long etudiantId, Long eventId) {
        Inscription inscription = inscriptionRepository.findByEtudiantIdAndEventId(etudiantId, eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée."));
        if (inscription.getStatut() == Inscription.StatutInscription.ANNULEE)
            throw new InvalidRequestException("Cette inscription est déjà annulée.");
        inscription.setStatut(Inscription.StatutInscription.ANNULEE);
        inscriptionRepository.save(inscription);
    }

    public InscriptionDTO valider(Long id) { return updateStatut(id, Inscription.StatutInscription.CONFIRMEE); }
    public InscriptionDTO refuser(Long id) { return updateStatut(id, Inscription.StatutInscription.REFUSEE); }

    private InscriptionDTO updateStatut(Long id, Inscription.StatutInscription statut) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription non trouvée: " + id));
        inscription.setStatut(statut);
        return toDTO(inscriptionRepository.save(inscription));
    }

    public List<InscriptionDTO> getByEvent(Long eventId) {
        return inscriptionRepository.findByEventId(eventId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<InscriptionDTO> getByEtudiant(Long etudiantId) {
        return inscriptionRepository.findByEtudiantId(etudiantId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public InscriptionDTO toDTO(Inscription i) {
        return InscriptionDTO.builder()
                .id(i.getId()).dateInscription(i.getDateInscription()).statut(i.getStatut())
                .etudiantId(i.getEtudiant().getId()).etudiantNom(i.getEtudiant().getNom())
                .etudiantEmail(i.getEtudiant().getEmail())
                .eventId(i.getEvent().getId()).eventTitre(i.getEvent().getTitre()).build();
    }
}