package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;

public interface MessageInterface {
    MessageResponseDTO createMessage(MessageRequestDTO messageDTO,String email);
}
