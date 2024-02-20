package com.example.filesystem.service.implementation;

import com.example.filesystem.entity.MetaDataFile;
import com.example.filesystem.mapper.FileMapper;
import com.example.filesystem.model.FileDto;
import com.example.filesystem.service.FileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

//@Service
//@RequiredArgsConstructor
//public class FileServiceImpl implements FileService {
//    private final MinioClient minioClient;
//
//    @Override
//    public UUID uploadFile(MultipartFile file) {
//
//    }
//
//    @Override
//    public FileDto downloadFile(UUID id) {
//
//    }
//
//    @Override
//    public List<FileDto> getAllFiles() {
//
//    }
//}
