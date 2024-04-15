package com.example.userapp.repository;

import com.example.userapp.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    void deleteAllByExpirationDateTimeBefore(Instant dateTime);
}
