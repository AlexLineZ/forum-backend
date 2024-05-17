package com.example.notificationservice.kafka;

import com.example.common.dto.NotificationUserMessage;
import com.example.notificationservice.service.KafkaMessageService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationListener {

    private final KafkaMessageService kafkaMessageService;

    @KafkaListener(topics = "${spring.kafka.topic.notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NotificationUserMessage message) throws MessagingException {
        kafkaMessageService.processNotification(message);
    }
}