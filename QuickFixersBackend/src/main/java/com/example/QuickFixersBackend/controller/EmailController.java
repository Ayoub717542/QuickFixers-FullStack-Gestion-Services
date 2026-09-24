package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.services.serviceImpl.EmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@Controller
@RequestMapping("/api/admin/emails")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class EmailController{
    private final EmailService emailService;

    @PostMapping("/sendEmail")
    private ResponseEntity<String> sendEmail( @RequestParam String supportEmail, @RequestParam Long ticketId){
        emailService.sendTestEmail(supportEmail, ticketId);
        return ResponseEntity.ok("Email de test envoyé.");
    }


}