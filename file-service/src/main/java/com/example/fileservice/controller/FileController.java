package com.example.fileservice.controller;

import com.example.fileservice.dto.FileDto;
import com.example.fileservice.service.FileService;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

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

