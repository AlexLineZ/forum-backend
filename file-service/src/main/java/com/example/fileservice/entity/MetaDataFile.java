package com.example.fileservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_meta_data")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetaDataFile {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Long size;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "user_id")
    private UUID userId;
}

