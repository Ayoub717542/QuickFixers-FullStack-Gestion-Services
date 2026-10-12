package com.example.QuickFixersBackend.services.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    private static final String EMAILJS_URL =
            "https://api.emailjs.com/api/v1.0/email/send";

    private static final String FRONTEND_URL =
            "https://quickfixers-frontend-latest.onrender.com";

    private final RestClient restClient = RestClient.create();

    @Value("${emailjs.service-id}")
    private String serviceId;

    @Value("${emailjs.template-id}")
    private String templateId;

    @Value("${emailjs.template-ticket-id}")
    private String ticketTemplateId;

    @Value("${emailjs.public-key}")
    private String publicKey;

    @Value("${emailjs.private-key}")
    private String privateKey;

    /** Admin a créé un compte (client ou support) : envoi des identifiants. */
    public void sendAccountCredentials(String to, String fullName,
                                       String email, String role, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_email", to);
        params.put("subject", "Votre compte QuickFixers");
        params.put("to_name", fullName);
        params.put("account_email", email);
        params.put("password", password);
        params.put("role", role);
        sendWithTemplate(templateId, params);
    }

    /** Inscription d'un client : email de bienvenue. */
    public void sendWelcome(String to, String fullName) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_email", to);
        params.put("subject", "Bienvenue sur QuickFixers");
        params.put("to_name", fullName);
        params.put("account_email", to);
        params.put("password", "Celui que vous avez choisi à l'inscription");
        params.put("role", "CLIENT");
        sendWithTemplate(templateId, params);
    }

    /** Un ticket vient d'être assigné à un support. */
    public void sendTicketAssigned(String to, String supportName, Long ticketId,
                                   String titre, String priority, String description) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_email", to);
        params.put("support_name", supportName);
        params.put("ticket_id", String.valueOf(ticketId));
        params.put("ticket_title", titre);
        params.put("ticket_priority", priority);
        params.put("ticket_description", description);
        params.put("ticket_link", FRONTEND_URL + "/support/tickets/" + ticketId);
        sendWithTemplate(ticketTemplateId, params);
    }

    /** Bouton "email de test" de l'admin. */
    public void sendTestEmail(String supportEmail, Long ticketId) {
        Map<String, Object> params = new HashMap<>();
        params.put("to_email", supportEmail);
        params.put("support_name", "Support");
        params.put("ticket_id", String.valueOf(ticketId));
        params.put("ticket_title", "Ticket de test");
        params.put("ticket_priority", "MOYENNE");
        params.put("ticket_description", "Ceci est un email de test envoyé depuis l'administration.");
        params.put("ticket_link", FRONTEND_URL + "/support/tickets/" + ticketId);
        sendWithTemplate(ticketTemplateId, params);
    }

    private void sendWithTemplate(String template, Map<String, Object> params) {
        Map<String, Object> body = new HashMap<>();
        body.put("service_id", serviceId);
        body.put("template_id", template);
        body.put("user_id", publicKey);
        body.put("accessToken", privateKey);
        body.put("template_params", params);

        restClient.post()
                .uri(EMAILJS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        log.info("Email envoyé via EmailJS (template {}) à {}", template, params.get("to_email"));
    }
}