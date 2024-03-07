package com.example.forumcore.service.topic;

import com.example.forumcore.dto.CustomPage;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.entity.Topic;
import com.example.forumcore.exception.NotFoundException;
import com.example.forumcore.repository.CategoryRepository;
import com.example.forumcore.repository.TopicRepository;
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


    @Override
    @Transactional
    public UUID createTopic(TopicRequest topicRequest) {
        Topic topic = new Topic();
        topic.setName(topicRequest.name());
        topic.setCreatedBy(UUID.randomUUID()); //TODO: поправить после того, как сделаю авторизацию
        topic.setCategory(categoryRepository.findById(topicRequest.categoryId())
                .orElseThrow(() -> new NotFoundException("Category with ID " + topicRequest.categoryId() + " not found")));

        Topic savedTopic = topicRepository.save(topic);
        return savedTopic.getId();
    }

    @Override
    @Transactional
    public UUID updateTopic(UUID id, TopicRequest updatedTopic) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic with ID " + id + " not found"));
        topic.setName(updatedTopic.name());

        topic.setCategory(categoryRepository.findById(updatedTopic.categoryId())
                .orElseThrow(() -> new NotFoundException("Category with ID " + updatedTopic.categoryId() + " not found")));

        topicRepository.save(topic);
        return id;
    }

    @Override
    @Transactional
    public void deleteTopic(UUID id) {
        if (!topicRepository.existsById(id)) {
            throw new NotFoundException("Topic with ID " + id + " not found");
        }
        topicRepository.deleteById(id);
    }

    @Override
    public CustomPage<TopicResponse> getTopics(int page, int size) {
        Page<Topic> topics = topicRepository.findAll(PageRequest.of(page, size));
        return getTopicResponseCustomPage(topics);
    }

    @Override
    public CustomPage<TopicResponse> searchTopicsByName(String name, int page, int size) {
        Page<Topic> topics = topicRepository.findBySubstringInName(name, PageRequest.of(page, size));
        return getTopicResponseCustomPage(topics);
    }

    @NotNull
    private CustomPage<TopicResponse> getTopicResponseCustomPage(Page<Topic> topics) {
        List<TopicResponse> content = topics.getContent().stream()
                .map(topic -> new TopicResponse(
                        topic.getId(),
                        topic.getName(),
                        topic.getCreatedAt(),
                        topic.getModifiedAt(),
                        topic.getCreatedBy(),
                        topic.getCategory().getId()))
                .toList();
        return new CustomPage<>(content, topics.getNumber(), topics.getSize(), topics.getTotalElements());
    }
}
