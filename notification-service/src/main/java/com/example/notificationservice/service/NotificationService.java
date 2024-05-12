package com.example.notificationservice.service;

import com.example.common.dto.PageResponse;
import com.example.notificationservice.dto.NotificationCountResponse;
import com.example.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    PageResponse<NotificationResponse> getNotifications(UUID userId, String search, Pageable pageable);
    NotificationCountResponse getUnreadNotificationCount(UUID userId);
    void markAsRead(List<UUID> ids, UUID userId);
}
