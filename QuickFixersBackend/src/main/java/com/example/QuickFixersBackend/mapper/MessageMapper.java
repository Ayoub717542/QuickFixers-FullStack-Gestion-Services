package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;
import com.example.QuickFixersBackend.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toEntity(MessageRequestDTO dto);

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.nom", target = "senderNom")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "receiver.nom", target = "receiverNom")
    @Mapping(source = "ticket.id", target = "ticketId")
    MessageResponseDTO toDto(Message message);
}
