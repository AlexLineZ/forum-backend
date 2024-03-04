package com.example.forumcore.controller;

import com.example.forumcore.dto.CustomPage;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.entity.Topic;
import com.example.forumcore.service.topic.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/topics")
@RequiredArgsConstructor
@Tag(name = "Темы форума")
public class TopicController {
    private final TopicService topicService;

    @Operation(summary = "Создать тему")
    @PostMapping
    public ResponseEntity<UUID> createTopic(@RequestBody TopicRequest topic) {
        return ResponseEntity.ok(topicService.createTopic(topic));
    }

    @Operation(summary = "Обновить тему")
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateTopic(@PathVariable UUID id, @RequestBody TopicRequest topic) {
        return ResponseEntity.ok(topicService.updateTopic(id, topic));
    }

    @Operation(summary = "Удалить тему")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable UUID id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить страницу тем форума")
    @GetMapping
    public ResponseEntity<CustomPage<TopicResponse>> getTopics(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(topicService.getTopics(page, size));
    }

    @Operation(summary = "Найти темы по названию")
    @GetMapping("/search")
    public ResponseEntity<CustomPage<TopicResponse>> searchTopicsByName(@RequestParam String name,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(topicService.searchTopicsByName(name, page, size));
    }
}
