package com.example.fileservice.service;

import com.example.common.dto.FileDto;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

public interface FileService {
    UUID uploadFile(MultipartFile file);
    Pair<String, byte[]> downloadFile(UUID id);
    FileDto getFileInfo(UUID id) throws FileNotFoundException;
    List<FileDto> getAllFiles();
}
