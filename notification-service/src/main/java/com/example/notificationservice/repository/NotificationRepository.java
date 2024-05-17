package com.example.notificationservice.repository;

import com.example.notificationservice.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query(
            "SELECT n FROM Notification n WHERE n.userId = :userId AND n.displayInHistory = :displayInHistory AND " +
                    "(:search IS NULL OR :search = '' " +
                    "OR LOWER(n.label) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(n.message) LIKE LOWER(CONCAT('%', :search, '%')))"
    )
    Page<Notification> findByUserIdAndSearch(
            UUID userId,
            String search,
            boolean displayInHistory,
            Pageable pageable
    );

    long countByUserIdAndReadAndDisplayInHistory(UUID userId, boolean read, boolean displayInHistory);
}
