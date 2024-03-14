package com.example.forumcore.service.message;

import com.example.common.dto.UserDto;
import com.example.common.exception.AccessNotAllowedException;
import com.example.common.exception.NotFoundException;
import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.entity.Message;
import com.example.forumcore.enums.MessageSortType;
import com.example.forumcore.mapper.MessageMapper;
import com.example.forumcore.repository.MessageRepository;
import com.example.forumcore.repository.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public UUID createMessage(MessageCreateRequest request, UserDto user) {
        if (request.getTopicId() == null) {
            throw new IllegalStateException("Topic ID must not be null");
        }
        Message message = new Message();
        message.setText(request.getText());
        message.setAuthor(user.firstName() + " " + user.lastName());
        message.setTopic(topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new NotFoundException("Topic with ID " + request.getTopicId() + " not found")));
        message.setCreatedBy(user.id());
        Message savedMessage = messageRepository.save(message);
        return savedMessage.getId();
    }

    @Override
    @Transactional
    public UUID updateMessage(UUID id, MessageUpdateRequest request, UserDto user) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Message with ID " + id + " not found"));

        if (!message.getCreatedBy().equals(user.id())) {
            throw new AccessNotAllowedException("You do not have permission to update this message");
        }

        message.setText(request.getText());
        messageRepository.save(message);
        return id;
    }

    @Override
    @Transactional
    public void deleteMessage(UUID id, UserDto user) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Message with ID " + id + " not found"));

        if (!message.getCreatedBy().equals(user.id())) {
            throw new AccessNotAllowedException("You do not have permission to delete this message");
        }

        messageRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PageResponse<MessageResponse> getMessagesByTopic(UUID topicId, int page, int size, MessageSortType sortType) {
        if (page < 0) {
            page = 0;
        }

        if (size < 1) {
            size = 1;
        }
        Pageable pageable = PageRequest.of(page, size, sortType.toSort());
        Page<Message> messages = messageRepository.findByTopicId(topicId, pageable);

        List<MessageResponse> messageResponses = messages.getContent()
                .stream()
                .map(MessageMapper::mapMessageToResponse)
                .toList();

        return new PageResponse<>(messageResponses, page, size, messages.getTotalElements());
    }

    @Override
    @Transactional
    public PageResponse<MessageResponse> searchMessages(
            String text,
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            String createdBy,
            UUID topicId,
            UUID categoryId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Message> spec = Specification.where(null);
        if (text != null) {
            spec = spec.and(MessageSpecification.textContains(text));
        }
        if (dateFrom != null && dateTo != null) {
            spec = spec.and(MessageSpecification.createdBetween(dateFrom.toLocalDate(), dateTo.toLocalDate()));
        }
        if (createdBy != null) {
            spec = spec.and(MessageSpecification.authorIs(createdBy));
        }
        if (topicId != null) {
            spec = spec.and(MessageSpecification.hasTopicId(topicId));
        }
        if (categoryId != null) {
            spec = spec.and(MessageSpecification.inCategory(categoryId));
        }

        Page<Message> messages = messageRepository.findAll(spec, pageable);
        List<MessageResponse> messageResponses = messages.getContent()
                .stream()
                .map(MessageMapper::mapMessageToResponse)
                .toList();

        return new PageResponse<>(messageResponses, page, size, messages.getTotalElements());
    }
}
