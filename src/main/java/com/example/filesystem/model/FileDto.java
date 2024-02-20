package com.example.filesystem.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDto {
    private String id;
    private String name;
    private long size;
    private LocalDateTime uploadTime;
}
