package com.example.notificationservice.service;

import com.example.common.dto.NotificationUserMessage;

public interface KafkaMessageService {
    void processNotification(NotificationUserMessage message);
}
