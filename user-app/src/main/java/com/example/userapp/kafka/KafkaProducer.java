package com.example.userapp.kafka;

import com.example.common.dto.NotificationUserMessage;
import com.example.common.dto.UserNotification;
import com.example.common.enums.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, NotificationUserMessage> kafkaTemplate;

    @Value("${spring.kafka.topic.notification}")
    private String topic;

    public void sendMessage(
            UserNotification user,
            String title,
            String content,
            List<NotificationChannel> channels,
            Boolean displayInHistory
    ) {
        NotificationUserMessage message = new NotificationUserMessage(
                user,
                title,
                content,
                displayInHistory,
                channels
        );

        kafkaTemplate.send(topic, message);
    }
}
