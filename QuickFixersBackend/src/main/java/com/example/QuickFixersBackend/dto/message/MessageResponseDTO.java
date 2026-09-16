package com.example.QuickFixersBackend.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponseDTO {
    private Long id;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private Long senderId;
    private String senderNom;
    private Long receiverId;
    private String receiverNom;
    private Long ticketId;
}
