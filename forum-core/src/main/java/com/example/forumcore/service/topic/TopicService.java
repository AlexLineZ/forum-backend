package com.example.forumcore.service.topic;

import com.example.common.dto.UserDto;
import com.example.forumcore.dto.PageResponse;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.enums.TopicSortType;

import java.util.UUID;

public interface TopicService {

    UUID createTopic(TopicRequest topic, UserDto user);

    UUID updateTopic(UUID id, TopicRequest updatedTopic, UserDto user);

    void deleteTopic(UUID id, UserDto user);

    PageResponse<TopicResponse> getTopics(int page, int size, TopicSortType sortType);

    PageResponse<TopicResponse> searchTopicsByName(String name, int page, int size);
}
