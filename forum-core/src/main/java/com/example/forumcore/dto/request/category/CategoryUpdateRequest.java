package com.example.forumcore.dto.request.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryUpdateRequest {
    @NotNull(message = "Name must not be null")
    @Size(min = 1, message = "Text must have at least 1 character")
    private String name;
}
