package com.example.forumcore.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
public class MessageCreateRequest {

    @NotBlank(message = "Text must not be null")
    @Size(min = 1, message = "Text must have at least 1 character")
    private String text;

    @NotNull(message = "Topic ID is required")
    private String topicId;

    private List<UUID> attachmentIds;
}