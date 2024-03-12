package com.example.forumcore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Getter
@RequiredArgsConstructor
public enum MessageSortType {
    CREATED_AT_ASC("createdAt", Sort.Direction.ASC),
    CREATED_AT_DESC("createdAt", Sort.Direction.DESC);

    private final String field;
    private final Direction direction;

    public Sort toSort() {
        return Sort.by(new Sort.Order(direction, field));
    }
}
