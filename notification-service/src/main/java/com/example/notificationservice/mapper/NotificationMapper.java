package com.example.notificationservice.mapper;

import com.example.common.dto.NotificationMessage;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.entity.Notification;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationMapper {
    public static NotificationResponse convertToNotificationResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getLabel(),
                notification.getMessage(),
                notification.getDate(),
                notification.isRead()
        );
    }

    public static Notification mapMessageToNotification(NotificationMessage message) {
        Notification notification = new Notification();
        notification.setUserId(UUID.fromString(message.getUserId()));
        notification.setLabel(message.getTitle());
        notification.setMessage(message.getMessage());
        notification.setDate(LocalDateTime.now());
        notification.setDisplayInHistory(message.isDisplayInHistory());
        return notification;
    }
}
