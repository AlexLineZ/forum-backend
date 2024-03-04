package com.example.forumcore.dto.response;

import java.util.Date;
import java.util.UUID;

public record TopicResponse(
        UUID id,
        String name,
        Date createdAt,
        Date modifiedAt,
        String createdBy,
        UUID categoryId
) {
}
