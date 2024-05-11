package com.example.notificationservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum NotificationSortType {
    UNREAD_FIRST_DATE_DESC(Arrays.asList(Sort.Order.asc("read"), Sort.Order.desc("date"))),
    DATE_DESC(List.of(Sort.Order.desc("date"))),
    DATE_ASC(List.of(Sort.Order.asc("date"))),
    READ_DESC(List.of(Sort.Order.desc("read"))),
    READ_ASC(List.of(Sort.Order.asc("read")));

    private final List<Sort.Order> orders;

    public Sort toSort() {
        return Sort.by(orders);
    }
}


