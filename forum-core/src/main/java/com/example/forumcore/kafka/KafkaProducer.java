package com.example.forumcore.kafka;

import com.example.common.dto.NotificationUserMessage;
import com.example.common.enums.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, NotificationUserMessage> kafkaTemplate;

    @Value("${spring.kafka.topic.notification}")
    private String topic;

    public void sendMessage(
            UUID userId,
            String title,
            String content,
            List<NotificationChannel> channels,
            Boolean displayInHistory
    ) {
        NotificationUserMessage message = new NotificationUserMessage(
                userId,
                title,
                content,
                displayInHistory,
                channels
        );

        kafkaTemplate.send(topic, message);
    }
}
