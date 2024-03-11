package com.example.forumcore.service.message;

import com.example.forumcore.entity.Message;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

//https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl

public class MessageSpecification {
    public static Specification<Message> textContains(String text) {
        return (root, query, cb) -> {
            if (text == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.like(cb.lower(root.get("text")), "%" + text.toLowerCase() + "%");
        };
    }

    public static Specification<Message> createdBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.between(root.get("createdAt"), startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        };
    }

    public static Specification<Message> authorIs(String author) {
        return (root, query, cb) -> {
            if (author == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
        };
    }

    public static Specification<Message> hasTopicId(UUID topicId) {
        return (root, query, cb) -> {
            if (topicId == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("topic").get("id"), topicId);
        };
    }

    public static Specification<Message> inCategory(UUID categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("topic").get("category").get("id"), categoryId);
        };
    }
}