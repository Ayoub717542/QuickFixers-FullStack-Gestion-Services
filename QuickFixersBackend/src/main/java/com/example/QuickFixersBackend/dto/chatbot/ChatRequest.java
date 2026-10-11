package com.example.QuickFixersBackend.dto.chatbot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record  ChatRequest (
        @NotBlank(message = "Message is required")
        @Size(max = 1000, message = "Message is too long")
        String message
){
}
