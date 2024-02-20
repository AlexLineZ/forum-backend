package com.example.filesystem.controller;

import com.example.filesystem.model.FileDto;
import com.example.filesystem.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1//files/")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

//    @PostMapping("/upload")
//    public ResponseEntity<UUID> uploadFile(@RequestParam("file") MultipartFile file) {
//
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID id) {
//        FileDto fileDto = fileService.downloadFile(id);
//
//    }

}
