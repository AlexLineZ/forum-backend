package com.example.forumcore.service.message;

import com.example.common.dto.UserDto;
import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.enums.MessageSortType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    UUID createMessage(MessageCreateRequest request, UserDto user);

    UUID updateMessage(UUID id, MessageUpdateRequest request, UserDto user);

    void deleteMessage(UUID id, UserDto user);

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
    void addAttachmentToMessage(UUID messageId, UUID fileId, UserDto user);

    void deleteAttachment(UUID messageId, UUID attachmentId, UserDto user);
}

