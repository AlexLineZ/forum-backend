package com.example.forumcore.service.category;

import com.example.forumcore.dto.request.category.CategoryCreateRequest;
import com.example.forumcore.dto.request.category.CategoryUpdateRequest;
import com.example.forumcore.dto.response.CategoryResponse;
import com.example.userapp.entity.User;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    UUID createCategory(CategoryCreateRequest request, User user);

    UUID updateCategory(UUID id, CategoryUpdateRequest request, User user);

    void deleteCategory(UUID id, User user);

    List<CategoryResponse> getAllCategoriesHierarchy();

    List<CategoryResponse> searchCategoriesByName(String name);
}

