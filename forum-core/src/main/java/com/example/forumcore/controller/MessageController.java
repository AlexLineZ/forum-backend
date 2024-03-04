package com.example.forumcore.controller;

import com.example.forumcore.dto.CustomPage;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Сообщения темы")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "Создать новое сообщение в теме")
    public ResponseEntity<UUID> createMessage(@RequestBody MessageCreateRequest request) {
        return ResponseEntity.ok(messageService.createMessage(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать сообщение")
    public ResponseEntity<UUID> updateMessage(@PathVariable UUID id, @RequestBody MessageUpdateRequest request) {
        UUID messageId = messageService.updateMessage(id, request);
        return ResponseEntity.ok(messageId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сообщение")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/topic/{topicId}")
    @Operation(summary = "Дать сообщения темы")
    public ResponseEntity<CustomPage<MessageResponse>> getMessagesByTopic(
            @PathVariable UUID topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPage<MessageResponse> messages = messageService.getMessagesByTopic(topicId, page, size);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/search")
    @Operation(summary = "Найти сообщения по критериям")
    public ResponseEntity<CustomPage<MessageResponse>> searchMessages(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) UUID topicId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        CustomPage<MessageResponse> messages = messageService
                .searchMessages(text, dateFrom, dateTo, createdBy, topicId, categoryId, page, size);
        return ResponseEntity.ok(messages);
    }
}
