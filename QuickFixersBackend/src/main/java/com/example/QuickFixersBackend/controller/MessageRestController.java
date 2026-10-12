package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;
import com.example.QuickFixersBackend.services.serviceInterfce.MessageInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageRestController {

    private final MessageInterface messageInterface;

    // Recupere l'historique des messages d'un ticket.
    @GetMapping("/ticket/{ticketId}")
    public List<MessageResponseDTO> getTicketMessages(
            @PathVariable Long ticketId,
            @AuthenticationPrincipal UserDetails user) {
        return messageInterface.getTicketMessages(ticketId, user.getUsername());
    }
}
