package com.example.forumcore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomPage<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
}
