package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;

import java.util.List;

public interface MessageInterface {

    MessageResponseDTO createMessage(MessageRequestDTO messageDTO, String email);

    List<MessageResponseDTO> getTicketMessages(Long ticketId, String email);

    // Messages recus par cet utilisateur et pas encore lus (utilise par la cloche).
    List<MessageResponseDTO> getUnreadMessages(String email);

    // Marque comme lus les messages recus par cet utilisateur sur un ticket.
    void markTicketRead(Long ticketId, String email);
}
