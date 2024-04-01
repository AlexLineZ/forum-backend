package com.example.fileservice.repository;

import com.example.fileservice.entity.MetaDataFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetaDataFileRepository extends JpaRepository<MetaDataFile, UUID> {
}

