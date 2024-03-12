package com.example.forumcore.service.message;

import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.enums.MessageSortType;
import com.example.userapp.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MessageService {

    UUID createMessage(MessageCreateRequest request, User user);

    UUID updateMessage(UUID id, MessageUpdateRequest request, User user);

    void deleteMessage(UUID id, User user);

    PageResponse<MessageResponse> getMessagesByTopic(UUID topicId, int page, int size, MessageSortType sortType);

    PageResponse<MessageResponse> searchMessages(
            String text,
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            String createdBy,
            UUID topicId,
            UUID categoryId,
            int page,
            int size
    );
}

