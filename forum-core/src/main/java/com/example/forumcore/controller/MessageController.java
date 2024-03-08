package com.example.forumcore.controller;

import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.service.message.MessageService;
import com.example.userapp.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/forum/messages")
@RequiredArgsConstructor
@Tag(name = "Сообщения темы")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "Создать новое сообщение в теме")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Не авторизирован",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UUID> createMessage(
            @RequestBody MessageCreateRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(messageService.createMessage(request, user));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Не авторизирован",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Нет доступа",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Объект не найден",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @Operation(summary = "Редактировать сообщение")
    public ResponseEntity<UUID> updateMessage(
            @PathVariable UUID id,
            @RequestBody MessageUpdateRequest request,
            @AuthenticationPrincipal User user
    ) {
        UUID messageId = messageService.updateMessage(id, request, user);
        return ResponseEntity.ok(messageId);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Не авторизирован",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Нет доступа",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Объект не найден",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @Operation(summary = "Удалить сообщение")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        messageService.deleteMessage(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/topic/{topicId}")
    @Operation(summary = "Дать сообщения темы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<PageResponse<MessageResponse>> getMessagesByTopic(
            @PathVariable UUID topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<MessageResponse> messages = messageService.getMessagesByTopic(topicId, page, size);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/search")
    @Operation(summary = "Найти сообщения по критериям")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<PageResponse<MessageResponse>> searchMessages(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) UUID topicId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<MessageResponse> messages = messageService
                .searchMessages(text, dateFrom, dateTo, createdBy, topicId, categoryId, page, size);
        return ResponseEntity.ok(messages);
    }
}
