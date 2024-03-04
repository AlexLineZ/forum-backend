package com.example.forumcore.service.topic;

import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TopicService {

    UUID createTopic(TopicRequest topic);

    UUID updateTopic(UUID id, TopicRequest updatedTopic);

    void deleteTopic(UUID id);

    Page<TopicResponse> getTopics(int page, int size);

    Page<TopicResponse> searchTopicsByName(String name, int page, int size);
}
