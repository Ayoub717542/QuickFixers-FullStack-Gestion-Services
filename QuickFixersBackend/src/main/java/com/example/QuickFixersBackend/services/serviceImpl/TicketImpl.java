package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;

import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.mapper.TicketMapper;
import com.example.QuickFixersBackend.entity.Admin;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.repository.ServiceRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.TicketInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketImpl implements TicketInterface {

    private  final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final EmailService emailService;

    @Override
    public TicketResponseDTO ajouterTeckit(TicketRequestDTO ticketRequestDTO , Long serviceId, String email) {
        Ticket ticket = ticketMapper.toEntity(ticketRequestDTO);

        Person createdBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("service not found"));

        ticket.setCreatedBy(createdBy);
        ticket.setService(service);
        ticket.setStatut(Statut.OUVERT);
        ticket.setDateCreation(LocalDateTime.now());

        List<Support> supports = userRepository.findSupportOrderByOpenTicketsAsc(service.getType());
        Support support = supports.isEmpty() ? null : supports.get(0);
        ticket.setAssignedTo(support);

        Ticket savedTicket = ticketRepository.save(ticket);

        if (support != null) {
            try {
                emailService.sendEmail(
                        support.getEmail(),
                        "Nouveau ticket #" + savedTicket.getId(),
                        "Bonjour " + support.getNom() + ",\n\n" +
                                "Un nouveau ticket vous a été assigné :\n" +
                                "Titre : " + savedTicket.getTitre() + "\n" +
                                "Description : " + savedTicket.getDescription() + "\n\n" +
                                "Merci de vous connecter pour le traiter."
                );
            } catch (Exception e) {
                System.out.println("Email non envoyé : " + e.getMessage());
            }
        }
            return ticketMapper.toDto(savedTicket);
    }

    @Override
        public TicketResponseDTO modifieTeckit(Long id, TicketRequestDTO ticketRequestDTO){
            Ticket ticket = ticketRepository.findById(id).orElseThrow(()-> new RuntimeException("ticket Not Found"));

            ticket.setTitre(ticketRequestDTO.getTitre());
            ticket.setDescription(ticketRequestDTO.getDescription());

            Ticket savedTicket = ticketRepository.save(ticket);

            return ticketMapper.toDto(savedTicket);
        }

    @Override
    public TicketResponseDTO consulterTeckit(Long id, Person person) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));
        if (person instanceof Admin) {
            return ticketMapper.toDto(ticket);
        }
        if (ticket.getCreatedBy() != null && ticket.getCreatedBy().getEmail().equals(person.getEmail())) {
            return ticketMapper.toDto(ticket);
        }
        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getEmail().equals(person.getEmail())) {
            return ticketMapper.toDto(ticket);
        }
        throw new RuntimeException("Access denied");
    }

    @Override
    public Page<TicketResponseDTO> listerTeckits(Person person, Pageable pageable) {
        Page<Ticket> tickets;

        if (person instanceof Admin) {
            tickets = ticketRepository.findAll(pageable);
        } else {
            tickets = ticketRepository.findByCreatedBy(person, pageable);
        }

        if (person instanceof Support) {
            return ticketRepository.findByAssignedTo(person , pageable).map(ticketMapper::toDto);
        }

        return tickets.map(ticketMapper::toDto);
    }

    @Override
    public TicketResponseDTO modifierStatut(Person person, Long ticketId, Statut statut) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));

        if(person instanceof Admin ){
            ticket.setStatut(statut);
        } else if (person instanceof Support) {
            if (ticket.getAssignedTo() == null
                    || !ticket.getAssignedTo().getEmail().equals(person.getEmail())){
                throw new RuntimeException("Access denied");
            }
            ticket.setStatut(statut);
        }else{
            throw new RuntimeException("Access denied");
        }
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Override
    public Page<TicketResponseDTO> filtrerParStatut(Person person,Statut statut,Pageable pageable) {
        if (person instanceof Admin) {
            return ticketRepository.findByStatut(statut, pageable)
                    .map(ticketMapper::toDto);
        }

        if (person instanceof Support) {
            return ticketRepository.findByAssignedToAndStatut(
                    person, statut, pageable
            ).map(ticketMapper::toDto);
        }

        return ticketRepository.findByCreatedByAndStatut(person, statut, pageable)
                .map(ticketMapper::toDto);
    }

    @Override
    public Page<TicketResponseDTO> rechercherTickets(Person person,String recherche, Pageable pageable) {
        if (person instanceof Admin) {
            return ticketRepository.searchedTicket(recherche, pageable)
                    .map(ticketMapper::toDto);
        }
        if (person instanceof Support) {
            return ticketRepository.searchedAssignedTickets(
                    recherche, person, pageable
            ).map(ticketMapper::toDto);
        }

        return ticketRepository.searchedUserTickets(recherche, person, pageable)
                .map(ticketMapper::toDto);
    }

    @Override
    public long countTickets(Person person) {
        if (person instanceof Admin) {
            return ticketRepository.count();
        }
        if (person instanceof Support) {
            return ticketRepository.countByAssignedTo(person);
        }
        if (person instanceof Client) {
            return ticketRepository.countByCreatedBy(person);
        }
        throw new RuntimeException("Access denied");
    }



}