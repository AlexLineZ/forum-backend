package com.example.forumcore.dto.request.category;

import java.util.UUID;
import java.util.Optional;

public record CategoryCreateRequest(String name, Optional<UUID> parentCategoryId) {}
