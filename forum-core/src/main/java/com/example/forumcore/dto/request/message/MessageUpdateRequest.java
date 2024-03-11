package com.example.forumcore.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageUpdateRequest {
    @NotBlank(message = "Text must not be null")
    @Size(min = 1, message = "Text must have at least 1 character")
    public String text;
}
