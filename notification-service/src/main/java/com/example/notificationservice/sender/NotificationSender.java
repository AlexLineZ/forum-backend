package com.example.notificationservice.sender;

import com.example.common.dto.NotificationUserMessage;
import jakarta.mail.MessagingException;

public interface NotificationSender {
    void send(NotificationUserMessage message) throws MessagingException;
}
