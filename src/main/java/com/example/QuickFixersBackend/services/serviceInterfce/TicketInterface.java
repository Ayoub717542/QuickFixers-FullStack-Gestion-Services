package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.entity.User;
import org.springframework.data.domain.Pageable;import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.enums.Statut;
import org.springframework.data.domain.Page;


public interface TicketInterface {
    TicketResponseDTO ajouterTeckit(TicketRequestDTO ticketRequestDTO , Long serviceId, String email);
     TicketResponseDTO modifieTeckit(Long id , TicketRequestDTO ticketRequestDTO);
     TicketResponseDTO consulterTeckit(Long id);
     Page<TicketResponseDTO> listerTeckits(User user,Pageable pageable);
     TicketResponseDTO modifierStatut(Long ticketId, Statut statut);
     Page<TicketResponseDTO> filtrerParStatut(Statut statut,Pageable pageable);
     Page<TicketResponseDTO> rechercherTickets(String recherche, Pageable pageable);
    long countTickets();

}
