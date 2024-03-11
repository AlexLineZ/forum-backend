package com.example.forumcore.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Data
public class CategoryCreateRequest {
    @NotBlank(message = "Name must not be null")
    @Size(min = 1, message = "Text must have at least 1 character")
    private String name;

    private UUID parentCategoryId;
}
