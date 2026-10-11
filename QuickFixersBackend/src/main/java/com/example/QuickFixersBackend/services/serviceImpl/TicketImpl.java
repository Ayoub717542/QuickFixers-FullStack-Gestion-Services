package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;

import com.example.QuickFixersBackend.enums.Priority;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.exception.NotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketImpl implements TicketInterface {

    private  final TicketMapper ticketMapper;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final EmailService emailService;

    @Override
    @CacheEvict(
            value = {"tickets", "ticketsByStatus", "ticketsByPriority", "ticketSearch", "ticketCount"},
            allEntries = true
    )
    public TicketResponseDTO ajouterTeckit(TicketRequestDTO ticketRequestDTO , Long serviceId, String email) {
        Ticket ticket = ticketMapper.toEntity(ticketRequestDTO);

        Person createdBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("service not found"));

        ticket.setCreatedBy(createdBy);
        ticket.setService(service);
        ticket.setStatut(Statut.OUVERT);
        ticket.setDateCreation(LocalDateTime.now());

        // Priorité choisie par le client, sinon valeur par défaut.
        if (ticket.getPriority() == null) {
            ticket.setPriority(Priority.MOYENNE);
        }

        List<Support> supports = userRepository.findSupportOrderByOpenTicketsAsc(service.getType());
        if (supports.isEmpty()) {
            supports = userRepository.findAnySupportOrderByOpenTicketsAsc();
        }
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
                                "Priorité : " + savedTicket.getPriority().getLabel() + "\n" +
                                "Description : " + savedTicket.getDescription() + "\n\n" +
                                "Merci de vous connecter pour le traiter."
                );
            } catch (Exception e) {
                log.warn("Email non envoyé au support : {}", e.getMessage());
            }
        }
            return toDto(savedTicket);
    }

    @Override
    @CacheEvict(
            value = {"tickets", "ticketsByStatus", "ticketsByPriority", "ticketSearch", "ticketCount"},
            allEntries = true
    )
        public TicketResponseDTO modifieTeckit(Long id, Person person, TicketRequestDTO ticketRequestDTO){
            Ticket ticket = ticketRepository.findById(id).orElseThrow(()-> new NotFoundException("ticket Not Found"));
            if (person instanceof Admin) {
            } else if (ticket.getCreatedBy() != null
                    && ticket.getCreatedBy().getEmail().equals(person.getEmail())) {
            } else {
                throw new AccessDeniedException("Access denied");
            }

            ticket.setTitre(ticketRequestDTO.getTitre());
            ticket.setDescription(ticketRequestDTO.getDescription());

            Ticket savedTicket = ticketRepository.save(ticket);

            return toDto(savedTicket);
        }

    @Override
    public TicketResponseDTO consulterTeckit(Long id, Person person) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket Not Found"));
        if (person instanceof Admin) {
            return toDto(ticket);
        }
        if (ticket.getCreatedBy() != null && ticket.getCreatedBy().getEmail().equals(person.getEmail())) {
            return toDto(ticket);
        }
        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getEmail().equals(person.getEmail())) {
            return toDto(ticket);
        }
        throw new AccessDeniedException("Access denied");
    }

    @Override
    @Cacheable(
            value = "tickets",
            key = "#p0.getClass().getSimpleName() + ':' + #p0.id"
                    + " + ':' + #p1.toString()"
    )
    public Page<TicketResponseDTO> listerTeckits(Person person, Pageable pageable) {
        Page<Ticket> tickets;

        if (person instanceof Admin) {
            tickets = ticketRepository.findAll(pageable);
        } else {
            tickets = ticketRepository.findByCreatedBy(person, pageable);
        }

        if (person instanceof Support) {
            return ticketRepository.findByAssignedTo(person , pageable).map(this::toDto);
        }

        return tickets.map(this::toDto);
    }

    @Override
    @CacheEvict(
            value = {"tickets", "ticketsByStatus", "ticketsByPriority", "ticketSearch", "ticketCount"},
            allEntries = true
    )
    public TicketResponseDTO modifierStatut(Person person, Long ticketId, Statut statut) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket Not Found"));

        if(person instanceof Admin ){
            ticket.setStatut(statut);
        } else if (person instanceof Support) {
            if (ticket.getAssignedTo() == null
                    || !ticket.getAssignedTo().getEmail().equals(person.getEmail())){
                throw new AccessDeniedException("Access denied");
            }
            ticket.setStatut(statut);
        }else{
            throw new AccessDeniedException("Access denied");
        }
        return toDto(ticketRepository.save(ticket));
    }

    @Override
    @CacheEvict(
            value = {"tickets", "ticketsByStatus", "ticketsByPriority", "ticketSearch", "ticketCount"},
            allEntries = true
    )
    public TicketResponseDTO modifierPriorite(Person person, Long ticketId, Priority priority) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket Not Found"));

        // Seul un administrateur peut (re)définir la priorité d'un ticket.
        if (!(person instanceof Admin)) {
            throw new AccessDeniedException("Access denied: only an admin can change the priority");
        }

        ticket.setPriority(priority);
        return toDto(ticketRepository.save(ticket));
    }

    @Override
    @Cacheable(
            value = "ticketsByStatus",
            key = "#p0.getClass().getSimpleName() + ':' + #p0.id"
                    + " + ':' + #p1 + ':' + #p2.toString()"
    )
    public Page<TicketResponseDTO> filtrerParStatut(Person person,Statut statut,Pageable pageable) {
        if (person instanceof Admin) {
            return ticketRepository.findByStatut(statut, pageable)
                    .map(this::toDto);
        }

        if (person instanceof Support) {
            return ticketRepository.findByAssignedToAndStatut(
                    person, statut, pageable
            ).map(this::toDto);
        }

        return ticketRepository.findByCreatedByAndStatut(person, statut, pageable)
                .map(this::toDto);
    }

    @Override
    @Cacheable(
            value = "ticketsByPriority",
            key = "#p0.getClass().getSimpleName() + ':' + #p0.id"
                    + " + ':' + #p1 + ':' + #p2.toString()"
    )
    public Page<TicketResponseDTO> filtrerParPriorite(Person person, Priority priority, Pageable pageable) {
        if (person instanceof Admin) {
            return ticketRepository.findByPriority(priority, pageable)
                    .map(this::toDto);
        }

        if (person instanceof Support) {
            return ticketRepository.findByAssignedToAndPriority(person, priority, pageable)
                    .map(this::toDto);
        }

        return ticketRepository.findByCreatedByAndPriority(person, priority, pageable)
                .map(this::toDto);
    }

    @Override
    @Cacheable(
            value = "ticketSearch",
            key = "#p0.getClass().getSimpleName() + ':' + #p0.id"
                    + " + ':' + #p1 + ':' + #p2.toString()"
    )
    public Page<TicketResponseDTO> rechercherTickets(Person person,String recherche, Pageable pageable) {
        if (person instanceof Admin) {
            return ticketRepository.searchedTicket(recherche, pageable)
                    .map(this::toDto);
        }
        if (person instanceof Support) {
            return ticketRepository.searchedAssignedTickets(
                    recherche, person, pageable
            ).map(this::toDto);
        }

        return ticketRepository.searchedUserTickets(recherche, person, pageable)
                .map(this::toDto);
    }

    @Override
    @Cacheable(
            value = "ticketCount",
            key = "#p0.getClass().getSimpleName() + ':' + #p0.id"
    )
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
        throw new AccessDeniedException("Access denied");
    }

    /**
     * Mappe un ticket vers son DTO et enrichit la priorité avec sa logique métier :
     * libellé lisible, délai cible (SLA) et détection automatique du retard.
     */
    private TicketResponseDTO toDto(Ticket ticket) {
        TicketResponseDTO dto = ticketMapper.toDto(ticket);

        Priority priority = ticket.getPriority() != null ? ticket.getPriority() : Priority.MOYENNE;
        dto.setPriority(priority);
        dto.setPriorityLabel(priority.getLabel());
        dto.setSlaHeures(priority.getSlaHeures());

        boolean termine = ticket.getStatut() == Statut.RESOLU || ticket.getStatut() == Statut.FERME;
        if (ticket.getDateCreation() != null) {
            LocalDateTime echeance = ticket.getDateCreation().plusHours(priority.getSlaHeures());
            dto.setEcheance(echeance.toLocalDate());
            dto.setEnRetard(!termine && LocalDateTime.now().isAfter(echeance));
        } else {
            dto.setEnRetard(false);
        }

        return dto;
    }

}
