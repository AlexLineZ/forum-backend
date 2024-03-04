package com.example.forumcore.service.topic;

import com.example.forumcore.dto.CustomPage;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;

import java.util.UUID;

public interface TopicService {

    UUID createTopic(TopicRequest topic);

    UUID updateTopic(UUID id, TopicRequest updatedTopic);

    void deleteTopic(UUID id);

    CustomPage<TopicResponse> getTopics(int page, int size);

    CustomPage<TopicResponse> searchTopicsByName(String name, int page, int size);
}
