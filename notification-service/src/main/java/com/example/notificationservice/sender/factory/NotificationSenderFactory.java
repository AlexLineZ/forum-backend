package com.example.notificationservice.sender.factory;

import com.example.common.enums.NotificationChannel;
import com.example.notificationservice.sender.implementation.EmailSender;
import com.example.notificationservice.sender.NotificationSender;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationSenderFactory {
    private final EmailSender emailSender;

    private final Map<NotificationChannel, NotificationSender> senders = new EnumMap<>(NotificationChannel.class);

    @PostConstruct
    public void init() {
        senders.put(NotificationChannel.EMAIL, emailSender);
    }

    public NotificationSender getSender(NotificationChannel channel) {
        return senders.get(channel);
    }
}
