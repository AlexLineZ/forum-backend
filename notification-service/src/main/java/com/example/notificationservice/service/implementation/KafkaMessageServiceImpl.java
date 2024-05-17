package com.example.notificationservice.service.implementation;

import com.example.common.dto.NotificationUserMessage;
import com.example.common.enums.NotificationChannel;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.mapper.NotificationMapper;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.EmailSender;
import com.example.notificationservice.service.KafkaMessageService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageServiceImpl implements KafkaMessageService {

    private final NotificationRepository notificationRepository;
    private final EmailSender emailSender;

    @Override
    public void processNotification(NotificationUserMessage message) throws MessagingException {
        System.out.println("Received notification: " + message.toString());

        Notification notification = NotificationMapper.mapMessageToNotification(message);
        notificationRepository.save(notification);

        if (message.getDeliveryChannels().contains(NotificationChannel.EMAIL)){
            emailSender.sendMessageToEmail(message);
        }
    }
}
