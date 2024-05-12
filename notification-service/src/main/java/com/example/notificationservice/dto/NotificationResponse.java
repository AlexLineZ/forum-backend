package com.example.notificationservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
    UUID id,
    String label,
    String message,
    LocalDateTime date,
    boolean read
) { }
