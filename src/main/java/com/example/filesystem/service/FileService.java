package com.example.filesystem.service;

import com.example.filesystem.model.FileDto;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    UUID uploadFile(MultipartFile file);
    Pair<String, byte[]> downloadFile(UUID id);
    List<FileDto> getAllFiles();
}
