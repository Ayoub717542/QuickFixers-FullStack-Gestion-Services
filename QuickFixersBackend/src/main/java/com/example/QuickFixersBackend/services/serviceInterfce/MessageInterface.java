package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;

import java.util.List;

public interface MessageInterface {

    MessageResponseDTO createMessage(MessageRequestDTO messageDTO, String email);

    List<MessageResponseDTO> getTicketMessages(Long ticketId, String email);
}
