package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.DTO.ServiceRequistDTO;
import com.example.QuickFixersBackend.DTO.TicketResponseDTO;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.services.serviceInterfce.TicketInterface;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class TicketImpl implements TicketInterface {

    @Override
    public TicketResponseDTO modifieTeckit(Long id, ServiceRequistDTO serviceRequistDTO) {
        return null;
    }

    @Override
    public TicketResponseDTO consulterTeckit(Long id) {
        return null;
    }

    @Override
    public Page<TicketResponseDTO> listerTeckits(Pageable pageable) {
        return null;
    }

    @Override
    public TicketResponseDTO assignerTicket(Long ticketId, Long supportId) {
        return null;
    }

    @Override
    public TicketResponseDTO modifierStatut(Long ticketId, Statut statut) {
        return null;
    }

    @Override
    public Page<TicketResponseDTO> filtrerParStatut(Statut statut) {
        return null;
    }

    @Override
    public Page<TicketResponseDTO> rechercherTickets(String recherche, Pageable pageable) {
        return null;
    }
}
