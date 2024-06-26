package com.example.forumcore.dto.response;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        Date createdAt,
        Date modifiedAt,
        UUID createdBy,
        String author,
        Optional<UUID> parentCategoryId,
        List<CategoryResponse> childCategories
) {}