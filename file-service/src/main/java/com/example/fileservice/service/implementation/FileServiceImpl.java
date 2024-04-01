package com.example.fileservice.service.implementation;

import com.example.fileservice.dto.FileDto;
import com.example.fileservice.entity.MetaDataFile;
import com.example.fileservice.exception.LoadFileException;
import com.example.fileservice.repository.MetaDataFileRepository;
import com.example.fileservice.service.FileService;
import com.example.fileservice.service.FilenameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MetaDataFileRepository metaDataFileRepository;
    private final MinioClient minioClient;
    private final FilenameService fileNameService;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public UUID uploadFile(MultipartFile file) {
        UUID fileId = UUID.randomUUID();
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileId.toString())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            MetaDataFile metaDataFile = new MetaDataFile(
                    fileId,
                    file.getOriginalFilename(),
                    file.getSize(),
                    LocalDateTime.now()
            );
            metaDataFileRepository.save(metaDataFile);

            return fileId;
        } catch (Exception e) {
            throw new LoadFileException("Error uploading file", e);
        }
    }

    @Override
    public Pair<String, byte[]> downloadFile(UUID id) {
        try {
            MetaDataFile metaDataFile = metaDataFileRepository.findById(id)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

            byte[] content;
            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(id.toString())
                            .build())) {
                content = inputStream.readAllBytes();
            }

            return Pair.of(fileNameService.convertFilename(metaDataFile.getName()), content);
        } catch (Exception e) {
            throw new LoadFileException("Error downloading file", e);
        }
    }

    @Override
    public List<FileDto> getAllFiles() {
        return metaDataFileRepository.findAll().stream().map(file -> {
            FileDto dto = new FileDto();
            dto.setId(file.getId().toString());
            dto.setName(file.getName());
            dto.setSize(file.getSize());
            dto.setUploadTime(file.getUploadTime());
            return dto;
        }).toList();
    }
}

