package com.example.QuickFixersBackend.services.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo("jls4q.signup@inbox.testmail.app");
        message.setSubject("Test QuickFixers");
        message.setText("Bonjour ! Ceci est un email de test.");

        mailSender.send(message);
    }

}
