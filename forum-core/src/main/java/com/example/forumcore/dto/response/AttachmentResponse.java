package com.example.forumcore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AttachmentResponse {
    private UUID id;
    private String name;
    private Long sizeInBytes;
    private UUID fileId;
}
