package com.example.forumcore.service.message;

import com.example.forumcore.dto.CustomPage;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    UUID createMessage(MessageCreateRequest request);

    UUID updateMessage(UUID id, MessageUpdateRequest request);

    void deleteMessage(UUID id);

    CustomPage<MessageResponse> getMessagesByTopic(UUID topicId, int page, int size);

    CustomPage<MessageResponse> searchMessages(
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

