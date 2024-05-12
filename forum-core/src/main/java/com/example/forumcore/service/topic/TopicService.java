package com.example.forumcore.service.topic;

import com.example.common.dto.PageResponse;
import com.example.common.dto.UserDto;
import com.example.forumcore.dto.request.topic.TopicRequest;
import com.example.forumcore.dto.response.TopicResponse;
import com.example.forumcore.enums.TopicSortType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TopicService {

    UUID createTopic(TopicRequest topic, UserDto user);

    UUID updateTopic(UUID id, TopicRequest updatedTopic, UserDto user);

    void deleteTopic(UUID id, UserDto user);

    PageResponse<TopicResponse> getTopics(int page, int size, TopicSortType sortType);

    PageResponse<TopicResponse> searchTopicsByName(String name, int page, int size);

    void addToFavorites(UUID topicId, UserDto user);

    void removeFromFavorites(UUID topicId, UserDto user);

    PageResponse<TopicResponse> getFavoriteTopics(UserDto user, Pageable pageable);
}
