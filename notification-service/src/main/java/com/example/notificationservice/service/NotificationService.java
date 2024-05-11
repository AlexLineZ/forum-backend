package com.example.notificationservice.service;

import com.example.common.dto.PageResponse;
import com.example.notificationservice.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    PageResponse<Notification> getNotifications(UUID userId, Pageable pageable);
    long getUnreadNotificationCount(UUID userId);
    void markAsRead(List<UUID> ids);
}
