package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.enums.Role;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.mapper.TicketMapper;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.repository.ServiceRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.TicketInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketImpl implements TicketInterface {

    private  final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public TicketResponseDTO ajouterTeckit(TicketRequestDTO ticketRequestDTO , Long serviceId, String email) {
        Ticket ticket = ticketMapper.toEntity(ticketRequestDTO);

        User createdBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("service not found"));

        ticket.setCreatedBy(createdBy);
        ticket.setService(service);
        ticket.setStatut(Statut.OUVERT);
        ticket.setDateCreation(LocalDateTime.now());

        List<User> supports = userRepository.findSupportOrderByOpenTicketsAsc(service.getType());
        User support = supports.isEmpty() ? null : supports.get(0);
        ticket.setAssignedTo(support);

        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
        public TicketResponseDTO modifieTeckit(Long id, TicketRequestDTO ticketRequestDTO){
            Ticket ticket = ticketRepository.findById(id).orElseThrow(()-> new RuntimeException("ticket Not Found"));

            ticket.setTitre(ticketRequestDTO.getTitre());
            ticket.setDescription(ticketRequestDTO.getDescription());

            Ticket savedTicket = ticketRepository.save(ticket);

            return ticketMapper.toDto(savedTicket);
        }

    @Override
    public TicketResponseDTO consulterTeckit(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));
        return ticketMapper.toDto(ticket);
    }

    @Override
    public Page<TicketResponseDTO> listerTeckits(User user, Pageable pageable) {
        Page<Ticket> tickets;

        if (user.getRole() == Role.ADMIN) {
            tickets = ticketRepository.findAll(pageable);
        } else {
            tickets = ticketRepository.findByCreatedBy(user, pageable);
        }

        return tickets.map(ticketMapper::toDto);
    }

    @Override
    public TicketResponseDTO modifierStatut(Long ticketId, Statut statut) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));

        ticket.setStatut(statut);

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toDto(savedTicket);
    }

    @Override
    public Page<TicketResponseDTO> filtrerParStatut(Statut statut,Pageable pageable) {
        return ticketRepository.findByStatut(statut, pageable)
                .map(ticketMapper::toDto);
    }

    @Override
    public Page<TicketResponseDTO> rechercherTickets(String recherche, Pageable pageable) {
        return ticketRepository.searchedTicket(recherche,pageable)
                .map(ticketMapper::toDto);
    }

    @Override
    public long countTickets() {
        return ticketRepository.count();
    }
}
