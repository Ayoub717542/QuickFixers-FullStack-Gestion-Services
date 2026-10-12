package com.example.QuickFixersBackend.services;

import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.exception.NotFoundException;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketAccessService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public Ticket requireTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));
    }

    public Person requireUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void requireMember(Ticket ticket, Person user) {
        boolean isCreator = ticket.getCreatedBy() != null
                && ticket.getCreatedBy().getId().equals(user.getId());

        boolean isAssigned = ticket.getAssignedTo() != null
                && ticket.getAssignedTo().getId().equals(user.getId());

        if (!isCreator && !isAssigned) {
            throw new AccessDeniedException(
                    "You are not allowed to access this ticket's conversation");
        }
    }

    public Ticket requireTicketMember(Long ticketId, String email) {
        Ticket ticket = requireTicket(ticketId);
        requireMember(ticket, requireUser(email));
        return ticket;
    }
}
