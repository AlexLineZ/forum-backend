package com.example.forumcore.controller;

import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.service.topic.TopicService;
import com.example.userapp.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/forum/topics")
@RequiredArgsConstructor
@Tag(name = "Темы форума")
public class TopicController {
    private final TopicService topicService;

    @Operation(summary = "Создать тему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Не авторизирован",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<UUID> createTopic(
            @Valid @RequestBody TopicRequest topic,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(topicService.createTopic(topic, user));
    }

    @Operation(summary = "Обновить тему")
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
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateTopic(
            @PathVariable UUID id,
            @Valid @RequestBody TopicRequest topic,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(topicService.updateTopic(id, topic, user));
    }

    @Operation(summary = "Удалить тему")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        topicService.deleteTopic(id, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить страницу тем форума")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<PageResponse<TopicResponse>> getTopics(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(topicService.getTopics(page, size));
    }

    @Operation(summary = "Найти темы по названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/search")
    public ResponseEntity<PageResponse<TopicResponse>> searchTopicsByName(@RequestParam String name,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(topicService.searchTopicsByName(name, page, size));
    }
}
