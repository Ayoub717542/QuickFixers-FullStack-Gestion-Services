package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.DTO.ServiceRequistDTO;
import com.example.QuickFixersBackend.DTO.TicketResponseDTO;
import com.example.QuickFixersBackend.enums.Statut;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface TicketInterface {
     TicketResponseDTO modifieTeckit(Long id , ServiceRequistDTO serviceRequistDTO);
     TicketResponseDTO consulterTeckit(Long id);
     Page<TicketResponseDTO> listerTeckits(Pageable pageable);
     TicketResponseDTO assignerTicket(Long ticketId, Long supportId);
     TicketResponseDTO modifierStatut(Long ticketId, Statut statut);
     Page<TicketResponseDTO> filtrerParStatut(Statut statut);
     Page<TicketResponseDTO> rechercherTickets(String recherche, Pageable pageable);

}
