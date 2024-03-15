package com.example.forumcore.enums;

import com.example.forumcore.dto.response.CategoryResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;

import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public enum CategorySortType {
    NAME_ASC("name", Direction.ASC),
    NAME_DESC("name", Direction.DESC),
    CREATED_AT_ASC("createdAt", Direction.ASC),
    CREATED_AT_DESC("createdAt", Direction.DESC);

    private final String field;
    private final Direction direction;

    public Comparator<CategoryResponse> toSort() {
        return switch (this) {
            case CREATED_AT_DESC -> Comparator.comparing(CategoryResponse::createdAt).reversed();
            case NAME_ASC -> Comparator.comparing(CategoryResponse::name);
            case NAME_DESC -> Comparator.comparing(CategoryResponse::name).reversed();
            default -> Comparator.comparing(CategoryResponse::createdAt);
        };
    }
}
