package com.example.forumcore.dto.response;

import java.util.Date;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        String text,
        UUID topicId,
        Date createdAt,
        Date modifiedAt,
        UUID createdBy
) {
}
