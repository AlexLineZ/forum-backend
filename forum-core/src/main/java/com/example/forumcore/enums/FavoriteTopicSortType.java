package com.example.forumcore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum FavoriteTopicSortType {
    ADDED_AT_ASC("added_at", Sort.Direction.ASC),
    ADDED_AT_DESC("added_at", Sort.Direction.DESC);

    private final String field;
    private final Sort.Direction direction;

    public Sort toSort() {
        return Sort.by(new Sort.Order(direction, field));
    }
}
