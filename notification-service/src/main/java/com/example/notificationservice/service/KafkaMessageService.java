package com.example.notificationservice.service;

import com.example.common.dto.NotificationMessage;

public interface KafkaMessageService {
    void processNotification(NotificationMessage message);
}
