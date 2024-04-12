package com.example.forumcore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageResponse {
    private UUID id;
    private String text;
    private UUID topicId;
    private Date createdAt;
    private Date modifiedAt;
    private UUID createdBy;
    private String author;
    private List<AttachmentResponse> attachments;
}
