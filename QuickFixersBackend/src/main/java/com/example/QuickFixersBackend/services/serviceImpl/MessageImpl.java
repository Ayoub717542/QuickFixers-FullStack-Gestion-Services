package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.message.MessageRequestDTO;
import com.example.QuickFixersBackend.dto.message.MessageResponseDTO;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.Message;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.mapper.MessageMapper;
import com.example.QuickFixersBackend.repository.MessageRepository;
import com.example.QuickFixersBackend.services.TicketAccessService;
import com.example.QuickFixersBackend.services.serviceInterfce.MessageInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageImpl implements MessageInterface {

    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final TicketAccessService ticketAccessService;

    @Override
    public MessageResponseDTO createMessage(MessageRequestDTO messageDTO, String email) {
        Ticket ticket = ticketAccessService.requireTicket(messageDTO.getTicketId());

        Person sender = ticketAccessService.requireUser(email);

        ticketAccessService.requireMember(ticket, sender);

        Message message = messageMapper.toEntity(messageDTO);
        message.setTicket(ticket);
        message.setSender(sender);
        message.setReceiver(findReceiver(ticket, sender));

        return messageMapper.toDto(messageRepository.save(message));
    }

    @Override
    public List<MessageResponseDTO> getTicketMessages(Long ticketId, String email) {
        Ticket ticket = ticketAccessService.requireTicket(ticketId);
        Person user = ticketAccessService.requireUser(email);
        ticketAccessService.requireMember(ticket, user);

        return messageRepository.findByTicketIdOrderByDateEnvoiAsc(ticketId)
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }

    private Person findReceiver(Ticket ticket, Person sender) {
        return sender instanceof Client ? ticket.getAssignedTo() : ticket.getCreatedBy();
    }
}
