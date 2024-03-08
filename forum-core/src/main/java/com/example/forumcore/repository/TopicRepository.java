package com.example.forumcore.repository;

import com.example.forumcore.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
    @Query("SELECT t FROM Topic t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :substring, '%'))")
    Page<Topic> findBySubstringInName(String substring, Pageable pageable);
    List<Topic> findByCategoryId(UUID categoryId);
    boolean existsByCategoryId(UUID categoryId);
}
