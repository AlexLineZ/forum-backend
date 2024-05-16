package com.example.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "label", nullable = false, length = 100)
    private String label;

    @Column(name = "message", nullable = false, length = 1024)
    private String message;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "read", nullable = false)
    private boolean read = false;

    @Column(name = "display_in_history", nullable = false)
    private boolean displayInHistory = true;
}
