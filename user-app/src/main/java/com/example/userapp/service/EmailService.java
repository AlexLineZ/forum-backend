package com.example.userapp.service;

import java.util.UUID;

public interface EmailService {
    void sendMessageToEmail(String email, UUID confirmationToken);
}
