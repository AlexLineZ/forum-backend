package com.example.notificationservice.mapper;

import com.example.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.entity.Notification;

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
}
