package com.example.forumcore.repository;

import com.example.forumcore.entity.FavoriteTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavoriteTopicRepository extends JpaRepository<FavoriteTopic, UUID> {
    Page<FavoriteTopic> findByUserId(UUID userId, Pageable pageable);
    boolean existsByUserIdAndTopicId(UUID userId, UUID topicId);
    void deleteByUserIdAndTopicId(UUID userId, UUID topicId);
}
