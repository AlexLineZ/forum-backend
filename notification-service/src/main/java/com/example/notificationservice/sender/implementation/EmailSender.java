package com.example.notificationservice.sender.implementation;

import com.example.common.dto.NotificationUserMessage;
import com.example.notificationservice.sender.NotificationSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender implements NotificationSender {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void send(NotificationUserMessage notification) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setSubject(notification.getTitle());
        helper.setText(notification.getMessage(), true);
        helper.setTo(notification.getUser().getEmail());
        helper.setFrom(emailFrom);

        sendEmail(message);
    }

    @Async
    protected void sendEmail(MimeMessage email) {
        javaMailSender.send(email);
    }
}
