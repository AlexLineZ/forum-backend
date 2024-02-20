package com.example.filesystem.mapper;

import com.example.filesystem.entity.MetaDataFile;
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
