package com.universite.eventplatform.service;

import com.universite.eventplatform.dto.NotificationDTO;
import com.universite.eventplatform.entity.Notification;
import com.universite.eventplatform.entity.User;
import com.universite.eventplatform.exception.ResourceNotFoundException;
import com.universite.eventplatform.repository.NotificationRepository;
import com.universite.eventplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationDTO createNotification(Long userId, String message, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + userId));
        Notification n = new Notification();
        n.setMessage(message);
        n.setUser(user);
        n.setEventId(eventId);
        n.setLue(false);
        return toDTO(notificationRepository.save(n));
    }

    public List<NotificationDTO> getByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndLueFalse(userId);
    }

    public void markAsRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification non trouvée: " + id));
        n.setLue(true);
        notificationRepository.save(n);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        list.forEach(n -> n.setLue(true));
        notificationRepository.saveAll(list);
    }

    public NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId()).message(n.getMessage()).lue(n.isLue())
                .createdAt(n.getCreatedAt()).userId(n.getUser().getId())
                .eventId(n.getEventId()).build();
    }
}
