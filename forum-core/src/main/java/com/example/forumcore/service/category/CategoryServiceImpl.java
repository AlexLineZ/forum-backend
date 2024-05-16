package com.example.forumcore.service.category;

import com.example.common.dto.user.UserDto;
import com.example.common.enums.Role;
import com.example.common.exception.AccessNotAllowedException;
import com.example.common.exception.NotFoundException;
import com.example.forumcore.dto.request.category.CategoryCreateRequest;
import com.example.forumcore.dto.request.category.CategoryUpdateRequest;
import com.example.forumcore.dto.response.CategoryResponse;
import com.example.forumcore.entity.Category;
import com.example.forumcore.entity.Topic;
import com.example.forumcore.enums.CategorySortType;
import com.example.forumcore.repository.CategoryRepository;
import com.example.forumcore.repository.MessageRepository;
import com.example.forumcore.repository.TopicRepository;
import com.example.security.client.UserAppClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final UserAppClient userAppClient;

    @Override
    @Transactional
    public UUID createCategory(CategoryCreateRequest request, UserDto user) {
        Category category = new Category();
        category.setName(request.getName());
        category.setCreatedBy(user.id());
        UUID parentCategoryId = request.getParentCategoryId();
        if (parentCategoryId != null) {
            Category parentCategory = categoryRepository.findById(parentCategoryId)
                    .orElseThrow(() -> new NotFoundException("Parent category with ID " + parentCategoryId + " not found"));

            boolean hasTopics = topicRepository.existsByCategoryId(parentCategoryId);
            if (hasTopics) {
                throw new IllegalStateException("Cannot assign a category with topics as a parent category");
            }
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);
        return category.getId();
    }


    @Override
    @Transactional
    public UUID updateCategory(UUID id, CategoryUpdateRequest request, UserDto user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (!category.getCreatedBy().equals(user.id()) && !user.role().equals(Role.ADMIN)) {
            throw new AccessNotAllowedException("User is not authorized to update this category");
        }

        category.setName(request.getName());
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id, UserDto user) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (!category.getCreatedBy().equals(user.id()) && !user.role().equals(Role.ADMIN)) {
            throw new AccessNotAllowedException("User is not authorized to delete this category");
        }

        deleteCategoryAndChildren(id);
    }


    @Override
    @Transactional
    public List<CategoryResponse> getAllCategoriesHierarchy(CategorySortType sortType) {
        List<Category> categories = categoryRepository.findAll();
        return buildHierarchy(categories, null, sortType);
    }

    @Override
    @Transactional
    public List<CategoryResponse> searchCategoriesByName(String name) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categories.stream()
                .map(category -> {
                    UserDto user = null;
                    try {
                        user = userAppClient.getUserById(category.getCreatedBy());
                    } catch (Exception e) {
                        System.err.println("Error fetching user details: " + e.getMessage());
                    }
                    String creatorFullName = (user != null) ? user.firstName() + " " + user.lastName() : "Unknown User";

                    return new CategoryResponse(
                            category.getId(),
                            category.getName(),
                            category.getCreatedAt(),
                            category.getModifiedAt(),
                            category.getCreatedBy(),
                            creatorFullName,
                            Optional.ofNullable(category.getParentCategory()).map(Category::getId),
                            Collections.emptyList()
                    );
                })
                .toList();
    }

    private List<CategoryResponse> buildHierarchy(List<Category> categories, UUID parentId, CategorySortType sortType) {
        List<CategoryResponse> hierarchy = new ArrayList<>();
        for (Category category : categories) {
            if ((parentId == null && category.getParentCategory() == null) ||
                    (category.getParentCategory() != null && Objects.equals(parentId, category.getParentCategory().getId()))) {
                List<CategoryResponse> childCategories = buildHierarchy(categories, category.getId(), sortType);
                UserDto user = null;
                try {
                    user = userAppClient.getUserById(category.getCreatedBy());
                } catch (Exception e) {
                    System.err.println("Error fetching user details: " + e.getMessage());
                }
                String creatorFullName = (user != null) ? user.firstName() + " " + user.lastName() : "Unknown User";

                CategoryResponse categoryResponse = new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getCreatedAt(),
                        category.getModifiedAt(),
                        category.getCreatedBy(),
                        creatorFullName,
                        Optional.ofNullable(category.getParentCategory()).map(Category::getId),
                        childCategories
                );
                hierarchy.add(categoryResponse);
            }
        }
        hierarchy.sort(sortType.toSort());
        return hierarchy;
    }

    private void deleteCategoryAndChildren(UUID categoryId) {
        List<Category> children = categoryRepository.findByParentCategoryId(categoryId);
        for (Category child : children) {
            deleteCategoryAndChildren(child.getId());
        }

        List<Topic> topics = topicRepository.findByCategoryId(categoryId);
        for (Topic topic : topics) {
            messageRepository.deleteByTopicId(topic.getId());
            topicRepository.deleteById(topic.getId());
        }

        categoryRepository.deleteById(categoryId);
    }
}
