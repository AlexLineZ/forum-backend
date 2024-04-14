package com.example.fileservice.controller;

import com.example.common.dto.FileDto;
import com.example.common.dto.UserDto;
import com.example.fileservice.service.FileService;
import com.google.common.net.HttpHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Работа с файлами")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка файла", description = "Позволяет загрузить файл на сервер")
    public ResponseEntity<UUID> uploadFile(
            @Parameter(description = "Файл для загрузки")
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDto user
    ) {
        UUID fileId = fileService.uploadFile(file, user);
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Скачивание файла", description = "Позволяет скачать файл по идентификатору")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable UUID id) {
        Pair<String, byte[]> fileData = fileService.downloadFile(id);
        ByteArrayResource resource = new ByteArrayResource(fileData.getSecond());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getFirst() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление файла", description = "Позволяет удалить файл по идентификатору")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID id, @AuthenticationPrincipal UserDto user) {
        fileService.deleteFile(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    @Operation(summary = "Получение информации о файле", description = "Получение информации о файле по идентификатору")
    public ResponseEntity<FileDto> getFileInfo(
            @Parameter(description = "Идентификатор файла") @RequestParam("id") UUID id
    ) throws FileNotFoundException {
        return ResponseEntity.ok(fileService.getFileInfo(id));
    }

    @GetMapping("/all")
    @Operation(summary = "Получение списка всех файлов", description = "Возвращает список всех файлов, доступных в системе")
    public ResponseEntity<List<FileDto>> getAllFiles() {
        List<FileDto> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }
}



