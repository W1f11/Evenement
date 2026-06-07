package com.universite.eventplatform.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean lue;
    private LocalDateTime createdAt;
    private Long userId;
    private Long eventId;
}
