package com.example.forumcore.dto.request;

import java.util.UUID;
import java.util.Optional;

public record CategoryCreateRequest(String name, Optional<UUID> parentCategoryId) {}
