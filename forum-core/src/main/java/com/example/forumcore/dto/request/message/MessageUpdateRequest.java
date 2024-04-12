package com.example.forumcore.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MessageUpdateRequest {
    @NotBlank(message = "Text must not be null")
    @Size(min = 1, message = "Text must have at least 1 character")
    private String text;
}
