package com.example.forumcore.service.message;

import com.example.common.dto.FileDto;
import com.example.common.dto.PageResponse;
import com.example.common.dto.UserDto;
import com.example.common.enums.NotificationChannel;
import com.example.common.enums.Role;
import com.example.common.exception.AccessNotAllowedException;
import com.example.common.exception.NotFoundException;
import com.example.forumcore.dto.request.message.MessageCreateRequest;
import com.example.forumcore.dto.request.message.MessageUpdateRequest;
import com.example.forumcore.dto.response.MessageResponse;
import com.example.forumcore.entity.Attachment;
import com.example.forumcore.entity.Message;
import com.example.forumcore.entity.Topic;
import com.example.forumcore.enums.MessageSortType;
import com.example.forumcore.kafka.KafkaProducer;
import com.example.forumcore.mapper.MessageMapper;
import com.example.forumcore.repository.AttachmentRepository;
import com.example.forumcore.repository.FavoriteTopicRepository;
import com.example.forumcore.repository.MessageRepository;
import com.example.forumcore.repository.TopicRepository;
import com.example.security.client.FileServiceClient;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final AttachmentRepository attachmentRepository;
    private final FavoriteTopicRepository favoriteTopicRepository;
    private final KafkaProducer kafkaProducer;
    private final FileServiceClient fileServiceClient;

    @Override
    @Transactional
    public UUID createMessage(MessageCreateRequest request, UserDto user) {
        if (request.getTopicId() == null) {
            throw new IllegalStateException("Topic ID must not be null");
        }

        Topic currentTopic = topicRepository.findById(toUUID(request.getTopicId()))
                .orElseThrow(() -> new NotFoundException("Topic with ID " + request.getTopicId() + " not found"));

        Message message = new Message();
        message.setText(request.getText());
        message.setAuthor(user.firstName() + " " + user.lastName());
        message.setTopic(currentTopic);
        message.setCreatedBy(user.id());


        if (request.getFilesIds() != null && !request.getFilesIds().isEmpty()) {
            Set<UUID> uniqueFileIds = new HashSet<>(request.getFilesIds());

            List<Attachment> attachments = uniqueFileIds.stream()
                    .map(fileId -> createAttachmentForMessage(fileId, message, user.id()))
                    .toList();

            attachmentRepository.saveAll(attachments);
        }

        List<UUID> userIdsWithFavoriteTopic = favoriteTopicRepository.findUserIdsByTopicId(toUUID(request.getTopicId()));
        userIdsWithFavoriteTopic.remove(user.id());

        for (UUID userId : userIdsWithFavoriteTopic) {
            kafkaProducer.sendMessage(
                    userId,
                    "Новый пост в вашем избранном топике: " + currentTopic.getName(),
                    request.getText(),
                    List.of(NotificationChannel.EMAIL),
                    true
            );
        }

        messageRepository.save(message);
        return message.getId();
    }

    @Override
    @Transactional
    public UUID updateMessage(UUID messageId, MessageUpdateRequest request, UserDto user) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message with ID " + messageId + " not found"));

        if (!message.getCreatedBy().equals(user.id())) {
            throw new AccessNotAllowedException("You do not have permission to update this message");
        }
        message.setText(request.getText());
        messageRepository.save(message);
        return messageId;
    }

    @Override
    @Transactional
    public void deleteMessage(UUID id, UserDto user) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Message with ID " + id + " not found"));

        if (!message.getCreatedBy().equals(user.id())
                && !user.role().equals(Role.ADMIN)
                && !user.role().equals(Role.MODERATOR)
        ) {
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

        List<MessageResponse> messageResponses = messages
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
        List<MessageResponse> messageResponses = messages
                .map(MessageMapper::mapMessageToResponse)
                .toList();

        return new PageResponse<>(messageResponses, page, size, messages.getTotalElements());
    }

    @Override
    @Transactional
    public void addAttachmentToMessage(UUID messageId, UUID fileId, UserDto user) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message with ID " + messageId + " not found"));

        if (!message.getCreatedBy().equals(user.id())) {
            throw new AccessNotAllowedException("Only the author of the message can add attachments");
        }

        Attachment attachment = createAttachmentForMessage(fileId, message, user.id());
        attachmentRepository.save(attachment);
    }

    @Override
    @Transactional
    public void deleteAttachment(UUID messageId, UUID attachmentId, UserDto user) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message with ID " + messageId + " not found"));

        if (!message.getCreatedBy().equals(user.id())
                && !user.role().equals(Role.ADMIN)
                && !user.role().equals(Role.MODERATOR)
        ) {
            throw new AccessNotAllowedException("You do not have permission to delete this attachment");
        }

        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment with ID " + attachmentId + " not found"));

        if (!attachment.getMessage().getId().equals(messageId)) {
            throw new IllegalArgumentException("Attachment does not belong to the message");
        }

        attachmentRepository.delete(attachment);

        try {
            fileServiceClient.deleteFile(attachment.getFileId(), user);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new NotFoundException("File with ID " + attachment.getFileId() + " not found in file service");
            } else {
                throw new IllegalArgumentException("Error communicating with file service", e);
            }
        }
    }

    private static UUID toUUID(String str) {
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Uncorrected format UUID: " + str, e);
        }
    }

    private Attachment createAttachmentForMessage(UUID fileId, Message message, UUID currentUserId){
        FileDto fileDto;
        try {
            fileDto = fileServiceClient.getFileInfo(fileId);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new NotFoundException("File with ID " + fileId + " not found in file service");
            } else {
                throw new IllegalArgumentException("Error communicating with file service", e);
            }
        }

        if (!currentUserId.equals(fileDto.getAuthorId())){
            throw new AccessNotAllowedException("Only the author of the file can add it to attachment");
        }

        return createAttachment(fileDto, message);
    }

    public static Attachment createAttachment(FileDto fileDto, Message message) {
        Attachment attachment = new Attachment();
        attachment.setMessage(message);
        attachment.setName(fileDto.getName());
        attachment.setSizeInBytes(fileDto.getSize());
        attachment.setFileId(UUID.fromString(fileDto.getId()));
        return attachment;
    }
}
