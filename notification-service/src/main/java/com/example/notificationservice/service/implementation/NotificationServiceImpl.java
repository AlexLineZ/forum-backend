package com.example.notificationservice.service.implementation;

import com.example.common.dto.PageResponse;
import com.example.notificationservice.dto.NotificationCountResponse;
import com.example.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.mapper.NotificationMapper;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public PageResponse<NotificationResponse> getNotifications(UUID userId, Pageable pageable) {
        Page<Notification> notificationsPage = notificationRepository.findByUserId(userId, pageable);

        List<Notification> unreadNotifications = notificationsPage.getContent().stream()
                .filter(notification -> !notification.isRead())
                .toList();

        markNotificationsAsRead(unreadNotifications);

        List<NotificationResponse> responses = notificationsPage.getContent().stream()
                .map(NotificationMapper::convertToNotificationResponse)
                .toList();

        return new PageResponse<>(
                responses,
                notificationsPage.getNumber(),
                notificationsPage.getSize(),
                notificationsPage.getTotalElements()
        );
    }

    private void markNotificationsAsRead(List<Notification> notifications) {
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Transactional(readOnly = true)
    public NotificationCountResponse getUnreadNotificationCount(UUID userId) {
        long count = notificationRepository.countByUserIdAndRead(userId, false);
        return new NotificationCountResponse(count);
    }

    @Transactional
    public void markAsRead(List<UUID> ids) {
        List<Notification> notifications = notificationRepository.findAllById(ids);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }
}

