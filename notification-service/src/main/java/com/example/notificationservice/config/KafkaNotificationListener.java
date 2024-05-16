package com.example.notificationservice.config;

import com.example.common.dto.NotificationMessage;
import com.example.notificationservice.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationListener {

    private final KafkaMessageService kafkaMessageService;

    @KafkaListener(topics = "${spring.kafka.topic.notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NotificationMessage message) {
        kafkaMessageService.processNotification(message);
    }
}