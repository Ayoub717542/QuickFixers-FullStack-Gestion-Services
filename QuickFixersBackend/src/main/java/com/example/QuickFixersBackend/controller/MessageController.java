package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;
import com.example.QuickFixersBackend.services.serviceInterfce.MessageInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageInterface messageInterface;

    @MessageMapping("/chat")
    public void sendMessage(MessageRequestDTO message, Principal principal) {
        MessageResponseDTO response =
                messageInterface.createMessage(message, principal.getName());

        messagingTemplate.convertAndSend("/topic/ticket." + message.getTicketId(), response);

        // Also deliver to the receiver's private inbox, so they get a toast + red badge
        // even when they are not inside the chat window.
        if (response.getReceiverEmail() != null) {
            messagingTemplate.convertAndSendToUser(
                    response.getReceiverEmail(), "/queue/messages", response);
        }
    }
}
