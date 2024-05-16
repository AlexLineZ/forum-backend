package com.example.forumcore.repository;

import com.example.forumcore.entity.FavoriteTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FavoriteTopicRepository extends JpaRepository<FavoriteTopic, UUID> {
    @Query("SELECT f.userId FROM FavoriteTopic f WHERE f.topic.id = :topicId")
    List<UUID> findUserIdsByTopicId(@Param("topicId") UUID topicId);

    Page<FavoriteTopic> findByUserId(UUID userId, Pageable pageable);

    boolean existsByUserIdAndTopicId(UUID userId, UUID topicId);

    void deleteByUserIdAndTopicId(UUID userId, UUID topicId);
}
