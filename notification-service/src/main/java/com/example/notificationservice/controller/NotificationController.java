package com.example.notificationservice.controller;

import com.example.common.dto.page.PageResponse;
import com.example.common.dto.user.UserDto;
import com.example.notificationservice.dto.response.NotificationCountResponse;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.enums.NotificationSortType;
import com.example.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Уведомления")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<PageResponse<NotificationResponse>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "UNREAD_FIRST_DATE_DESC") NotificationSortType sortType,
            @RequestParam(defaultValue = "") String search,
            @AuthenticationPrincipal UserDto user) {
        Sort sort = sortType.toSort();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(notificationService.getNotifications(user.id(), search, pageable));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<NotificationCountResponse> getUnreadNotificationCount(@AuthenticationPrincipal UserDto user) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(user.id()));
    }

    @PutMapping("/read")
    public ResponseEntity<Void> markAsRead(@RequestBody List<UUID> ids, @AuthenticationPrincipal UserDto user) {
        notificationService.markAsRead(ids, user.id());
        return ResponseEntity.ok().build();
    }
}
