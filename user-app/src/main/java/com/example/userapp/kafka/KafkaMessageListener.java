package com.example.userapp.kafka;

import com.example.common.dto.NotificationMessage;
import com.example.common.exception.UserNotFoundException;
import com.example.userapp.entity.User;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageListener {
    private final KafkaProducer kafkaProducer;
    private final UserRepository userRepository;

    @KafkaListener(topics = "${spring.kafka.topic.message}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NotificationMessage message) {
        User user = userRepository.findById(message.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + message.getUserId()));

        kafkaProducer.sendMessage(
                UserMapper.mapUserToUserNotification(user),
                message.getTitle(),
                message.getMessage(),
                message.getDeliveryChannels(),
                message.isDisplayInHistory()
        );
    }
}
