package com.example.forumcore.controller;

import com.example.common.dto.page.PageResponse;
import com.example.common.dto.user.UserDto;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.enums.FavoriteTopicSortType;
import com.example.forumcore.enums.TopicSortType;
import com.example.forumcore.service.topic.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @PostMapping
    public ResponseEntity<UUID> createTopic(
            @Valid @RequestBody TopicRequest topic,
            @AuthenticationPrincipal UserDto user
    ) {
        return ResponseEntity.ok(topicService.createTopic(topic, user));
    }

    @Operation(summary = "Обновить тему")
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateTopic(
            @PathVariable UUID id,
            @Valid @RequestBody TopicRequest topic,
            @AuthenticationPrincipal UserDto user
    ) {
        return ResponseEntity.ok(topicService.updateTopic(id, topic, user));
    }

    @Operation(summary = "Удалить тему")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable UUID id, @AuthenticationPrincipal UserDto user) {
        topicService.deleteTopic(id, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить страницу тем форума")
    @GetMapping
    public ResponseEntity<PageResponse<TopicResponse>> getTopics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CREATED_AT_DESC") TopicSortType sortType) {
        PageResponse<TopicResponse> topics = topicService.getTopics(page, size, sortType);
        return ResponseEntity.ok(topics);
    }

    @Operation(summary = "Найти темы по названию")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<TopicResponse>> searchTopicsByName(@RequestParam String name,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(topicService.searchTopicsByName(name, page, size));
    }

    @Operation(summary = "Добавление топика в избранное")
    @PostMapping("/favorites/{topicId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable UUID topicId, @AuthenticationPrincipal UserDto user) {
        topicService.addToFavorites(topicId, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление топика из избранного")
    @DeleteMapping("/favorites/{topicId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable UUID topicId, @AuthenticationPrincipal UserDto user) {
        topicService.removeFromFavorites(topicId, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение списка избранных топиков пользователя")
    @GetMapping("/favorites")
    public ResponseEntity<PageResponse<TopicResponse>> getFavoriteTopics(
            @AuthenticationPrincipal UserDto user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ADDED_AT_DESC") FavoriteTopicSortType sortType) {
        Sort sort = sortType.toSort();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<TopicResponse> favoriteTopics = topicService.getFavoriteTopics(user, pageable);
        return ResponseEntity.ok(favoriteTopics);
    }
}
