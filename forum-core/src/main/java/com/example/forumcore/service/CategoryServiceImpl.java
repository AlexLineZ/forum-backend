package com.example.forumcore.service;

import com.example.forumcore.dto.request.CategoryRequest;
import com.example.forumcore.entity.Category;
import com.example.forumcore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public UUID createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        request.parentCategoryId().ifPresent(id ->
                category.setParentCategory(categoryRepository.findById(id).orElse(null))
        );
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    @Transactional
    public UUID updateCategory(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(request.name());
        request.parentCategoryId().ifPresent(parentId ->
                category.setParentCategory(categoryRepository.findById(parentId).orElse(null))
        );
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Category> getAllCategoriesHierarchy() {
        return null;
    }

    @Override
    @Transactional
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}
