package com.example.filesystem.controller;

import com.example.filesystem.model.FileDto;
import com.example.filesystem.service.FileService;
import com.example.filesystem.service.implementation.FileServiceImpl;
import com.google.common.net.HttpHeaders;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileService;

    @PostMapping("/upload")
    public ResponseEntity<UUID> uploadFile(@RequestParam("file") MultipartFile file) {
        UUID fileId = fileService.uploadFile(file);
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable UUID id) {
        Pair<String, byte[]> fileData = fileService.downloadFile(id);
        ByteArrayResource resource = new ByteArrayResource(fileData.getSecond());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getFirst() + "\"")
                .body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileDto>> getAllFiles() {
        List<FileDto> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }
}

