package com.example.forumcore.dto.request.message;

import java.util.UUID;

public record MessageCreateRequest(String text, UUID topicId) {
}
