package com.example.forumcore.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AttachmentResponse {
    private UUID id;
    private String name;
    private Long sizeInBytes;
    private UUID fileId;

    public AttachmentResponse(UUID id, String name, Long sizeInBytes, UUID fileId) {
        this.id = id;
        this.name = name;
        this.sizeInBytes = sizeInBytes;
        this.fileId = fileId;
    }
}
