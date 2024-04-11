package com.example.forumcore.mapper;

import com.example.forumcore.dto.response.AttachmentResponse;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.entity.Message;

import java.util.List;

public class MessageMapper {
    public static MessageResponse mapMessageToResponse(Message message) {
        List<AttachmentResponse> attachmentResponses = message.getAttachments().stream()
                .map(attachment -> new AttachmentResponse(
                        attachment.getId(),
                        attachment.getName(),
                        attachment.getSizeInBytes(),
                        attachment.getFileId()
                ))
                .toList();

        return new MessageResponse(
                message.getId(),
                message.getText(),
                message.getTopic().getId(),
                message.getCreatedAt(),
                message.getModifiedAt(),
                message.getCreatedBy(),
                message.getAuthor(),
                attachmentResponses
        );
    }
}
