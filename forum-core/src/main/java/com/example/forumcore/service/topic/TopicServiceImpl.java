package com.example.forumcore.service.topic;

import com.example.common.client.UserAppClient;
import com.example.common.dto.UserDto;
import com.example.common.exception.AccessNotAllowedException;
import com.example.common.exception.NotFoundException;
import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.entity.Category;
import com.example.forumcore.entity.Topic;
import com.example.forumcore.enums.TopicSortType;
import com.example.forumcore.repository.CategoryRepository;
import com.example.forumcore.repository.MessageRepository;
import com.example.forumcore.repository.TopicRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final MessageRepository messageRepository;
    private final UserAppClient userAppClient;

    @Override
    @Transactional
    public UUID createTopic(TopicRequest topicRequest, UserDto user) {
        if (topicRequest.getCategoryId() == null) {
            throw new IllegalStateException("Category Id must not be null");
        }
        Category category = categoryRepository.findById(topicRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category with ID " + topicRequest.getCategoryId() + " not found"));

        boolean hasChildCategories = categoryRepository.existsByParentCategoryId(topicRequest.getCategoryId());
        if (hasChildCategories) {
            throw new IllegalStateException("Cannot create topic in a category that has child categories");
        }

        Topic topic = new Topic();
        topic.setName(topicRequest.getName());
        topic.setCreatedBy(user.id());
        topic.setCategory(category);

        Topic savedTopic = topicRepository.save(topic);
        return savedTopic.getId();
    }

    @Override
    @Transactional
    public UUID updateTopic(UUID id, TopicRequest updatedTopic, UserDto user) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic with ID " + id + " not found"));

        if (!topic.getCreatedBy().equals(user.id())) {
            throw new AccessNotAllowedException("You do not have permission to update this topic");
        }

        topic.setName(updatedTopic.getName());
        topic.setCategory(categoryRepository.findById(updatedTopic.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category with ID " + updatedTopic.getCategoryId() + " not found")));

        topicRepository.save(topic);
        return id;
    }

    @Override
    @Transactional
    public void deleteTopic(UUID id, UserDto user) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic with ID " + id + " not found"));

        if (!topic.getCreatedBy().equals(user.id())) {
            throw new AccessNotAllowedException("You do not have permission to delete this topic");
        }

        messageRepository.deleteByTopicId(id);
        topicRepository.deleteById(id);
    }

    @Override
    public PageResponse<TopicResponse> getTopics(int page, int size, TopicSortType sortType) {
        if (page < 0) {
            page = 0;
        }
        if (size < 1) {
            size = 1;
        }
        Pageable pageable = PageRequest.of(page, size, sortType.toSort());
        Page<Topic> topics = topicRepository.findAll(pageable);
        return getTopicResponseCustomPage(topics);
    }

    @Override
    public PageResponse<TopicResponse> searchTopicsByName(String name, int page, int size) {
        Page<Topic> topics = topicRepository.findBySubstringInName(name, PageRequest.of(page, size));
        return getTopicResponseCustomPage(topics);
    }

    @NotNull
    private PageResponse<TopicResponse> getTopicResponseCustomPage(Page<Topic> topics) {
        List<TopicResponse> content = topics.getContent().stream()
                .map(topic -> {
                    UserDto user = userAppClient.getUserById(topic.getCreatedBy());
                    String creatorFullName = user.firstName() + " " + user.lastName();

                    return new TopicResponse(
                            topic.getId(),
                            topic.getName(),
                            topic.getCreatedAt(),
                            topic.getModifiedAt(),
                            topic.getCreatedBy(),
                            creatorFullName,
                            topic.getCategory().getId()
                    );
                })
                .toList();
        return new PageResponse<>(content, topics.getNumber(), topics.getSize(), topics.getTotalElements());
    }
}
