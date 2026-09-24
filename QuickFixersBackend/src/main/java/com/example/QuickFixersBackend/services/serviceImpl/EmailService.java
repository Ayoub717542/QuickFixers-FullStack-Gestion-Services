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

    public void sendTestEmail(String supportEmail, Long ticketId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(supportEmail);
        message.setSubject("Ticket assigné - QuickFixers");
        message.setText(
                "Bonjour,\n\n" +
                        "Le ticket n°" + ticketId + " vous a été assigné.\n" +
                        "Connectez-vous à QuickFixers pour le consulter."
        );

        mailSender.send(message);
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendAccountCredentials(String to, String fullName,
                                       String email, String role, String password) {
        String text = "Bonjour " + fullName + ",\n\n" +
                "Votre compte a été créé sur QuickFixers.\n\n" +
                "Email : " + email + "\n" +
                "Mot de passe : " + password + "\n" +
                "Rôle : " + role + "\n\n" +
                "Vous pouvez maintenant vous connecter.";

        sendEmail(to, "Votre compte QuickFixers", text);
    }


}
