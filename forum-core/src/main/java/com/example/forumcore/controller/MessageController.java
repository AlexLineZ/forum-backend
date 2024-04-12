package com.example.forumcore.controller;

import com.example.common.dto.UserDto;
import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.enums.MessageSortType;
import com.example.forumcore.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/forum/messages")
@RequiredArgsConstructor
@Tag(name = "Сообщения темы")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Создать новое сообщение в теме")
    @PostMapping
    public ResponseEntity<UUID> createMessage(
            @Valid @RequestBody MessageCreateRequest request,
            @AuthenticationPrincipal UserDto user
    ) {
        return ResponseEntity.ok(messageService.createMessage(request, user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать сообщение")
    public ResponseEntity<UUID> updateMessage(
            @PathVariable UUID id,
            @Valid @RequestBody MessageUpdateRequest request,
            @AuthenticationPrincipal UserDto user
    ) {
        UUID messageId = messageService.updateMessage(id, request, user);
        return ResponseEntity.ok(messageId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сообщение")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id, @AuthenticationPrincipal UserDto user) {
        messageService.deleteMessage(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/topic/{topicId}")
    @Operation(summary = "Дать сообщения темы")
    public ResponseEntity<PageResponse<MessageResponse>> getMessagesByTopic(
            @PathVariable UUID topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CREATED_AT_DESC") MessageSortType sortType) {
        PageResponse<MessageResponse> messages = messageService.getMessagesByTopic(topicId, page, size, sortType);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/search")
    @Operation(summary = "Найти сообщения по критериям")
    public ResponseEntity<PageResponse<MessageResponse>> searchMessages(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) UUID topicId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Negative page") int page,
            @RequestParam(defaultValue = "10") @Positive(message = "Negative size") int size
    ) {
        PageResponse<MessageResponse> messages = messageService
                .searchMessages(text, dateFrom, dateTo, createdBy, topicId, categoryId, page, size);
        return ResponseEntity.ok(messages);
    }
}
