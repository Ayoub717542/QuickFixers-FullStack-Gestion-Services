package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.services.serviceInterfce.MessageInterface;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;



@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageInterface messageInterface;

    @MessageMapping("/chat")
    public void sendMessage(MessageRequestDTO message, @AuthenticationPrincipal Person person) {
        MessageResponseDTO responseDTO =messageInterface.createMessage(message,person.getEmail());

        messagingTemplate.convertAndSend(
                "/topic/ticket." + message.getTicketId(),
                responseDTO);
    }

}
