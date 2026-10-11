package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.chatbot.ChatRequest;
import com.example.QuickFixersBackend.dto.chatbot.ChatResponse;
import com.example.QuickFixersBackend.services.serviceInterfce.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) throws IOException {
        String answer = chatbotService.reply(request.message());
        return new ChatResponse(answer);
    }
}
