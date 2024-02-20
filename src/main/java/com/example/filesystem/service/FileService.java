package com.example.filesystem.service;

import com.example.filesystem.model.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileService {
    UUID uploadFile(MultipartFile file);
    FileDto downloadFile(UUID id);
    List<FileDto> getAllFiles();
}
