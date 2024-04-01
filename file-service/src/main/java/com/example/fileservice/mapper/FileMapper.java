package com.example.fileservice.mapper;

import com.example.fileservice.entity.MetaDataFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

public class FileMapper {
    public static MetaDataFile fileToMetaData(MultipartFile file){
        return new MetaDataFile(
                UUID.randomUUID(),
                file.getOriginalFilename(),
                file.getSize(),
                LocalDateTime.now()
        );
    }
}

