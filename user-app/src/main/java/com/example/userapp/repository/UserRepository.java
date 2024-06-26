package com.example.userapp.repository;

import com.example.userapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(Long phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(Long phone);
    User findByEmailIgnoreCase(String emailId);
}
