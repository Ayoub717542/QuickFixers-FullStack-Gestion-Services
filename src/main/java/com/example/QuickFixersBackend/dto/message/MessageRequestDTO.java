package com.example.QuickFixersBackend.dto.message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDTO {
    private String contenu;
    private Long receiverId;
    private Long ticketId;
}
