package com.example.forumcore.controller;

import com.example.common.dto.user.UserDto;
import com.example.forumcore.dto.request.category.CategoryCreateRequest;
import com.example.forumcore.dto.request.category.CategoryUpdateRequest;
import com.example.forumcore.dto.response.CategoryResponse;
import com.example.forumcore.enums.CategorySortType;
import com.example.forumcore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/forum/categories")
@RequiredArgsConstructor
@Tag(name = "Категории")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Создать новую категорию")
    @PostMapping
    public ResponseEntity<UUID> createCategory(
           @Valid @RequestBody CategoryCreateRequest categoryCreateRequest,
            @AuthenticationPrincipal UserDto user
    ) {
        return ResponseEntity.ok(categoryService.createCategory(categoryCreateRequest, user));
    }

    @Operation(summary = "Обновить категорию")
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCategory(@PathVariable UUID id,
                                               @Valid @RequestBody CategoryUpdateRequest categoryRequest,
                                               @AuthenticationPrincipal UserDto user
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest, user));
    }

    @Operation(summary = "Удалить категорию")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id, @AuthenticationPrincipal UserDto user) {
        categoryService.deleteCategory(id, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить иерархию категорий")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestParam(name = "sort", defaultValue = "NAME_ASC") CategorySortType sortType
    ) {
        return ResponseEntity.ok(categoryService.getAllCategoriesHierarchy(sortType));
    }

    @Operation(summary = "Найти категорию по имени")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategoriesByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name));
    }
}

