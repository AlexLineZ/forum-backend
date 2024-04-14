package com.example.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FileDto {
    private String id;
    private String name;
    private long size;
    private LocalDateTime uploadTime;
    private UUID authorId;
}
