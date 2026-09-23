package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.services.serviceImpl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/emails")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendTestEmail() {
        emailService.sendTestEmail();
        return ResponseEntity.ok("Email de test envoyé.");
    }
}