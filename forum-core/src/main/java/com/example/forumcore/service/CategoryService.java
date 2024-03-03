package com.example.forumcore.service;

import com.example.forumcore.dto.request.CategoryRequest;
import com.example.forumcore.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    UUID createCategory(CategoryRequest request);

    UUID updateCategory(UUID id, CategoryRequest request);

    void deleteCategory(UUID id);

    List<Category> getAllCategoriesHierarchy();

    List<Category> searchCategoriesByName(String name);
}

