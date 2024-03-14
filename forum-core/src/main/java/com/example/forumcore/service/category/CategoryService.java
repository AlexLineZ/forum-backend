package com.example.forumcore.service.category;

import com.example.common.dto.UserDto;
import com.example.forumcore.dto.request.category.CategoryCreateRequest;
import com.example.forumcore.dto.request.category.CategoryUpdateRequest;
import com.example.forumcore.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    UUID createCategory(CategoryCreateRequest request, UserDto user);

    UUID updateCategory(UUID id, CategoryUpdateRequest request, UserDto user);

    void deleteCategory(UUID id, UserDto user);

    List<CategoryResponse> getAllCategoriesHierarchy();

    List<CategoryResponse> searchCategoriesByName(String name);
}

