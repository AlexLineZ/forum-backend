package com.example.notificationservice.mapper;

import com.example.common.dto.NotificationUserMessage;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.entity.Notification;

import java.time.LocalDateTime;

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

    public static Notification mapMessageToNotification(NotificationUserMessage message) {
        Notification notification = new Notification();
        notification.setUserId(message.getUser().getUserId());
        notification.setLabel(message.getTitle());
        notification.setMessage(message.getMessage());
        notification.setDate(LocalDateTime.now());
        notification.setDisplayInHistory(message.isDisplayInHistory());
        return notification;
    }
}
