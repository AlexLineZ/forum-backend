package com.example.fileservice.mapper;

import com.example.common.dto.file.FileDto;
import com.example.fileservice.entity.MetaDataFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

public class FileMapper {
    public static MetaDataFile fileToMetaData(MultipartFile file, UUID userId){
        return new MetaDataFile(
                UUID.randomUUID(),
                file.getOriginalFilename(),
                file.getSize(),
                LocalDateTime.now(),
                userId
        );
    }

    public static FileDto metaToFileDto(MetaDataFile file){
        return new FileDto(
            file.getId().toString(),
                file.getName(),
                file.getSize(),
                file.getUploadTime(),
                file.getUserId()
        );
    }
}

