package com.example.forumcore.dto.response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        Optional<UUID> parentCategoryId,
        List<CategoryResponse> childCategories
) {}