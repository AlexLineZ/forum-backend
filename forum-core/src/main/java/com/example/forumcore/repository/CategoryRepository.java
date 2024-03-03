package com.example.forumcore.repository;

import com.example.forumcore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);

    List<Category> findByParentCategoryId(UUID parentCategoryId);
}
