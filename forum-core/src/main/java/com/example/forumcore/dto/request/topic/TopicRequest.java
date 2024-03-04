package com.example.forumcore.dto.request.topic;

import java.util.UUID;

public record TopicRequest(String name, UUID categoryId) { }
