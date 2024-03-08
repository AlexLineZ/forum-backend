package com.example.forumcore.repository;

import com.example.forumcore.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>, JpaSpecificationExecutor<Message> {
    Page<Message> findByTopicId(UUID topicId, Pageable pageable);
    void deleteByTopicId(UUID topicId);
}
