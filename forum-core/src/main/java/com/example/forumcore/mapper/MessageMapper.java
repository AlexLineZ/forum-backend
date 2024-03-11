package com.example.forumcore.mapper;

import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.entity.Message;

public class MessageMapper {
    public static MessageResponse mapMessageToResponse(Message message){
        return new MessageResponse(
                message.getId(),
                message.getText(),
                message.getTopic().getId(),
                message.getCreatedAt(),
                message.getModifiedAt(),
                message.getCreatedBy(),
                message.getAuthor()
        );
    }
}
