package com.example.forumcore.repository;

import com.example.forumcore.entity.FavoriteTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteTopicRepository extends JpaRepository<FavoriteTopic, UUID> {
    List<FavoriteTopic> findByUserId(UUID userId);
    boolean existsByUserIdAndTopicId(UUID userId, UUID topicId);
    void deleteByUserIdAndTopicId(UUID userId, UUID topicId);
}
