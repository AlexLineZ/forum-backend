package com.example.forumcore.service.topic;

import com.example.common.exception.AccessNotAllowedException;
import com.example.common.exception.NotFoundException;
import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.entity.Category;
import com.example.forumcore.entity.Topic;
import com.example.forumcore.repository.CategoryRepository;
import com.example.forumcore.repository.MessageRepository;
import com.example.forumcore.repository.TopicRepository;
import com.example.userapp.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public UUID createTopic(TopicRequest topicRequest, User user) {
        Category category = categoryRepository.findById(topicRequest.categoryId())
                .orElseThrow(() -> new NotFoundException("Category with ID " + topicRequest.categoryId() + " not found"));

        boolean hasChildCategories = categoryRepository.existsByParentCategoryId(topicRequest.categoryId());
        if (hasChildCategories) {
            throw new IllegalStateException("Cannot create topic in a category that has child categories");
        }

        Topic topic = new Topic();
        topic.setName(topicRequest.name());
        topic.setCreatedBy(user.getId());
        topic.setCategory(category);

        Topic savedTopic = topicRepository.save(topic);
        return savedTopic.getId();
    }

    @Override
    @Transactional
    public UUID updateTopic(UUID id, TopicRequest updatedTopic, User user) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic with ID " + id + " not found"));

        if (!topic.getCreatedBy().equals(user.getId())) {
            throw new AccessNotAllowedException("You do not have permission to update this topic");
        }

        topic.setName(updatedTopic.name());
        topic.setCategory(categoryRepository.findById(updatedTopic.categoryId())
                .orElseThrow(() -> new NotFoundException("Category with ID " + updatedTopic.categoryId() + " not found")));

        topicRepository.save(topic);
        return id;
    }

    @Override
    @Transactional
    public void deleteTopic(UUID id, User user) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic with ID " + id + " not found"));

        if (!topic.getCreatedBy().equals(user.getId())) {
            throw new AccessNotAllowedException("You do not have permission to delete this topic");
        }

        messageRepository.deleteByTopicId(id);
        topicRepository.deleteById(id);
    }

    @Override
    public PageResponse<TopicResponse> getTopics(int page, int size) {
        Page<Topic> topics = topicRepository.findAll(PageRequest.of(page, size));
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
                .map(topic -> new TopicResponse(
                        topic.getId(),
                        topic.getName(),
                        topic.getCreatedAt(),
                        topic.getModifiedAt(),
                        topic.getCreatedBy(),
                        topic.getCategory().getId()))
                .toList();
        return new PageResponse<>(content, topics.getNumber(), topics.getSize(), topics.getTotalElements());
    }
}
