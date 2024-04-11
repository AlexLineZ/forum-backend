package com.example.forumcore.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class MessageResponse {
    private UUID id;
    private String text;
    private UUID topicId;
    private Date createdAt;
    private Date modifiedAt;
    private UUID createdBy;
    private String author;
    private List<AttachmentResponse> attachments;

    public MessageResponse(UUID id, String text, UUID topicId, Date createdAt, Date modifiedAt, UUID createdBy, String author, List<AttachmentResponse> attachments) {
        this.id = id;
        this.text = text;
        this.topicId = topicId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.author = author;
        this.attachments = attachments;
    }
}
