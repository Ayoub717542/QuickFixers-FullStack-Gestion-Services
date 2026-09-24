package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.entity.Person;
import org.springframework.data.domain.Pageable;import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.enums.Statut;
import org.springframework.data.domain.Page;


public interface TicketInterface {
    TicketResponseDTO ajouterTeckit(TicketRequestDTO ticketRequestDTO , Long serviceId, String email);
     TicketResponseDTO modifieTeckit(Long id , TicketRequestDTO ticketRequestDTO);
     TicketResponseDTO consulterTeckit(Long id,Person person);
     Page<TicketResponseDTO> listerTeckits(Person person,Pageable pageable);
     TicketResponseDTO modifierStatut(Person person, Long ticketId, Statut statut);
     Page<TicketResponseDTO> filtrerParStatut(Person person, Statut statut,Pageable pageable);
     Page<TicketResponseDTO> rechercherTickets(Person person,String recherche, Pageable pageable);
    long countTickets(Person person);

}