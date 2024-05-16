package com.example.fileservice.service.implementation;

import com.example.common.dto.file.FileDto;
import com.example.common.dto.user.UserDto;
import com.example.common.enums.Role;
import com.example.common.exception.AccessNotAllowedException;
import com.example.fileservice.entity.MetaDataFile;
import com.example.fileservice.exception.LoadFileException;
import com.example.fileservice.mapper.FileMapper;
import com.example.fileservice.repository.MetaDataFileRepository;
import com.example.fileservice.service.FileService;
import com.example.fileservice.service.FilenameService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    @Transactional
    public UUID uploadFile(MultipartFile file, UserDto user) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            MetaDataFile metaDataFile = FileMapper.fileToMetaData(file, user.id());
            MetaDataFile newFile = metaDataFileRepository.save(metaDataFile);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(newFile.getId().toString())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return newFile.getId();
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
    public FileDto getFileInfo(UUID id) throws FileNotFoundException {
        MetaDataFile metaDataFile = metaDataFileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

        return FileMapper.metaToFileDto(metaDataFile);
    }

    @Override
    public void deleteFile(UUID id, UserDto user) {
        try {
            MetaDataFile metaDataFile = metaDataFileRepository.findById(id)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));

            if (!metaDataFile.getUserId().equals(user.id())
                    && !user.role().equals(Role.ADMIN)
                    && !user.role().equals(Role.MODERATOR)
            ) {
                throw new AccessNotAllowedException("You do not have permission to delete this file");
            }

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(id.toString())
                            .build());

            metaDataFileRepository.delete(metaDataFile);
        } catch (Exception e) {
            throw new LoadFileException("Error deleting file", e);
        }
    }


    @Override
    public List<FileDto> getAllFiles() {
        return metaDataFileRepository.findAll().stream().map(FileMapper::metaToFileDto).toList();
    }
}

