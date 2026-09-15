package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.services.serviceInterfce.MessageInterface;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageInterface messageInterface;

    @MessageMapping("/chat")
    public void sendMessage(MessageRequestDTO message, @AuthenticationPrincipal User user) {
        MessageResponseDTO responseDTO =messageInterface.createMessage(message,user.getEmail());

        messagingTemplate.convertAndSend(
                "/topic/ticket." + message.getTicketId(),
                responseDTO);
    }
    @PostMapping("/test")
    public MessageResponseDTO test(@RequestBody MessageRequestDTO message, @AuthenticationPrincipal User user) {
        return messageInterface.createMessage(message,user.getEmail());
    }

}
