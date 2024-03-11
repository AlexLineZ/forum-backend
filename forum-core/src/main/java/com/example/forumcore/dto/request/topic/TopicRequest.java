package com.example.forumcore.dto.request.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class TopicRequest {

    @NotBlank(message = "Name must not be null")
    @Size(min = 1, message = "Name must have at least 1 character")
    private String name;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;
}