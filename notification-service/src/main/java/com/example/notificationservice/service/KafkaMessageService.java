package com.example.notificationservice.service;

import com.example.common.dto.NotificationUserMessage;
import jakarta.mail.MessagingException;

public interface KafkaMessageService {
    void processNotification(NotificationUserMessage message) throws MessagingException;
}
