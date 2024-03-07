package com.example.forumcore.service.topic;

import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;

import java.util.UUID;

public interface TopicService {

    UUID createTopic(TopicRequest topic);

    UUID updateTopic(UUID id, TopicRequest updatedTopic);

    void deleteTopic(UUID id);

    PageResponse<TopicResponse> getTopics(int page, int size);

    PageResponse<TopicResponse> searchTopicsByName(String name, int page, int size);
}
