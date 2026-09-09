package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.mapper.TicketMapper;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.repository.ServiceRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.TicketInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class TicketImpl implements TicketInterface {

    private  final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    @Override
    public TicketResponseDTO ajouterTeckit(TicketRequestDTO ticketRequestDTO) {
        Ticket ticket = ticketMapper.toEntity(ticketRequestDTO);
        ticket.setStatut(Statut.OUVERT);
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }
        @Override
        public TicketResponseDTO modifieTeckit(Long id, TicketRequestDTO ticketRequestDTO){
            Ticket ticket = ticketRepository.findById(id).orElseThrow(()-> new RuntimeException("ticket Not Found"));

            ticket.setTitre(ticketRequestDTO.getTitre());
            ticket.setDescription(ticketRequestDTO.getDescription());
            ticket.setPrix(ticketRequestDTO.getPrix());
            ticket.setStatut(ticketRequestDTO.getStatut());

            User user = userRepository.findById(ticketRequestDTO.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User Not Found"));

            ticket.setAssignedTo(user);

            ServiceEntity service = serviceRepository.findById(ticketRequestDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service Not Found"));

            ticket.setService(service);

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
    public Page<TicketResponseDTO> listerTeckits(Pageable pageable) {
        return ticketRepository.findAll(pageable)
                .map(ticketMapper::toDto);
    }

    @Override
    public TicketResponseDTO assignerTicket(Long ticketId, Long supportId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));
        User support = userRepository.findById(supportId)
                .orElseThrow(() -> new RuntimeException("Support Not Found"));

        ticket.setAssignedTo(support);

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toDto(savedTicket);
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
}
