package com.example.forumcore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Getter
@RequiredArgsConstructor
public enum TopicSortType {
    CREATED_AT_ASC("createdAt", Direction.ASC),
    CREATED_AT_DESC("createdAt", Direction.DESC),
    NAME_ASC("name", Direction.ASC),
    NAME_DESC("name", Direction.DESC);

    private final String field;
    private final Direction direction;

    public Sort toSort() {
        return Sort.by(new Sort.Order(direction, field));
    }
}
