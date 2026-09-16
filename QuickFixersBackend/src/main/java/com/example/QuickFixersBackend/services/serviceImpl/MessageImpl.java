package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;
import com.example.QuickFixersBackend.entity.Message;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.enums.Role;
import com.example.QuickFixersBackend.mapper.MessageMapper;
import com.example.QuickFixersBackend.repository.MessageRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.MessageInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageImpl implements MessageInterface {
    private final TicketRepository ticketRepository;
    private final UserRepository  userRepository;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    @Override
    public MessageResponseDTO createMessage(MessageRequestDTO messageDTO, String email) {

        Ticket ticket = ticketRepository.findById(messageDTO.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User receiver = userRepository.findById(messageDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        User sender = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("email not found"));


        if (sender.getRole() == Role.USER &&
                !ticket.getCreatedBy().getId().equals(sender.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (sender.getRole() == Role.SUPPORT &&
                !ticket.getAssignedTo().getId().equals(sender.getId())) {
            throw new RuntimeException("Access denied");
        }

        Message message = messageMapper.toEntity(messageDTO);
        message.setContenu(messageDTO.getContenu());
        message.setTicket(ticket);
        message.setSender(sender);
        message.setReceiver(receiver);

Message saveMessage =messageRepository.save(message);

        return messageMapper.toDto(saveMessage);
    }
}
