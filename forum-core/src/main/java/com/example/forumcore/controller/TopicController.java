package com.example.forumcore.controller;

import com.example.forumcore.service.topic.TopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
@Tag(name = "Темы форума")
public class TopicController {
    private final TopicService topicService;
}
