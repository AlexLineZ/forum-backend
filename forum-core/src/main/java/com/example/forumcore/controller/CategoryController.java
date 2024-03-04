package com.example.forumcore.controller;

import com.example.forumcore.dto.request.category.CategoryCreateRequest;
import com.example.forumcore.dto.request.category.CategoryUpdateRequest;
import com.example.forumcore.dto.response.CategoryResponse;
import com.example.forumcore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
@Tag(name = "Категории")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Создать новую категорию")
    @PostMapping
    public ResponseEntity<UUID> createCategory(@RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ResponseEntity.ok(categoryService.createCategory(categoryCreateRequest));
    }

    @Operation(summary = "Обновить категорию")
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCategory(@PathVariable UUID id, @RequestBody CategoryUpdateRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest));
    }

    @Operation(summary = "Удалить категорию")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить иерархию категорий")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategoriesHierarchy());
    }

    @Operation(summary = "Найти категорию по имени")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategoriesByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name));
    }
}

