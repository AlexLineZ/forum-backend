package com.example.fileservice.service;

import com.example.common.dto.FileDto;
import com.example.common.dto.UserDto;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

public interface FileService {
    UUID uploadFile(MultipartFile file, UserDto user);
    Pair<String, byte[]> downloadFile(UUID id);
    FileDto getFileInfo(UUID id) throws FileNotFoundException;

    void deleteFile(UUID id, UserDto user);

    List<FileDto> getAllFiles();
}
