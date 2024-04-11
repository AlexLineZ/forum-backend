package com.example.userapp.service.implementation;

import com.example.userapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.userapp.config.MessageConfig.*;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendMessageToEmail(String email, UUID confirmationToken){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom(emailFrom);
        mailMessage.setSubject(SUBJECT_MESSAGE);
        mailMessage.setText(EMAIL_MESSAGE + CONFIRM_LINK + confirmationToken);
        sendEmail(mailMessage);
    }

    @Async
    protected void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}
