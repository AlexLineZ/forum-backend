package com.example.forumcore.service.category;

import com.example.forumcore.dto.request.category.CategoryCreateRequest;
import com.example.forumcore.dto.request.category.CategoryUpdateRequest;
import com.example.forumcore.dto.response.CategoryResponse;
import com.example.forumcore.entity.Category;
import com.example.forumcore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public UUID createCategory(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setCreatedBy("Zaglushka"); //TODO: изменить, когда добавлю авторизацию
        request.parentCategoryId().ifPresent(id ->
                category.setParentCategory(categoryRepository.findById(id).orElse(null))
        );
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    @Transactional
    public UUID updateCategory(UUID id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(request.name());
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        List<Category> children = categoryRepository.findByParentCategoryId(id);
        for (Category child : children) {
            child.setParentCategory(null);
            categoryRepository.save(child);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<CategoryResponse> getAllCategoriesHierarchy() {
        List<Category> categories = categoryRepository.findAll();
        return buildHierarchy(categories, null);
    }

    @Override
    @Transactional
    public List<CategoryResponse> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getCreatedAt(),
                        category.getModifiedAt(),
                        category.getCreatedBy(),
                        Optional.ofNullable(category.getParentCategory()).map(Category::getId),
                        Collections.emptyList()
                ))
                .toList();
    }

    private List<CategoryResponse> buildHierarchy(List<Category> categories, UUID parentId) {
        List<CategoryResponse> hierarchy = new ArrayList<>();
        for (Category category : categories) {
            if ((parentId == null && category.getParentCategory() == null) ||
                    (category.getParentCategory() != null && Objects.equals(parentId, category.getParentCategory().getId()))) {
                List<CategoryResponse> childCategories = buildHierarchy(categories, category.getId());
                CategoryResponse categoryResponse = new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getCreatedAt(),
                        category.getModifiedAt(),
                        category.getCreatedBy(),
                        Optional.ofNullable(category.getParentCategory()).map(Category::getId),
                        childCategories
                );
                hierarchy.add(categoryResponse);
            }
        }
        hierarchy.sort(Comparator.comparing(CategoryResponse::name));
        return hierarchy;
    }
}
