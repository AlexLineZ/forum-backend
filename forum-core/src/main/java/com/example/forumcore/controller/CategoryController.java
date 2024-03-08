package com.example.forumcore.controller;

import com.example.forumcore.dto.request.category.CategoryCreateRequest;
import com.example.forumcore.dto.request.category.CategoryUpdateRequest;
import com.example.forumcore.dto.response.CategoryResponse;
import com.example.forumcore.service.category.CategoryService;
import com.example.userapp.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Не авторизирован",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<UUID> createCategory(
            @RequestBody CategoryCreateRequest categoryCreateRequest,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.createCategory(categoryCreateRequest, user));
    }

    @Operation(summary = "Обновить категорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Не авторизирован",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Нет доступа",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Объект не найден",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCategory(@PathVariable UUID id,
                                               @RequestBody CategoryUpdateRequest categoryRequest,
                                               @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest, user));
    }

    @Operation(summary = "Удалить категорию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Не авторизирован",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Нет доступа",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Объект не найден",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        categoryService.deleteCategory(id, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить иерархию категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategoriesHierarchy());
    }

    @Operation(summary = "Найти категорию по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchCategoriesByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name));
    }
}

