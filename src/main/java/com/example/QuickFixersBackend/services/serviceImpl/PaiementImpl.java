package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import com.example.QuickFixersBackend.entity.Paiement;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.enums.PaiementStatut;
import com.example.QuickFixersBackend.mapper.PaiementMapper;
import com.example.QuickFixersBackend.repository.PaiementRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.PaiementInterface;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaiementImpl implements PaiementInterface {

    private final TicketRepository ticketRepository;
    private final PaiementMapper paiementMapper;
    private final PaiementRepository paiementRepository;

    @Override
    public PaiementResponseDTO creerPaiement(PaiementRequestDTO paiementRequestDTO) {
        Ticket ticket = ticketRepository.findById(paiementRequestDTO.getTicketId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        Paiement payment = paiementMapper.toEntity(paiementRequestDTO);
        payment.setTicket(ticket);
        payment.setDateCreation(LocalDateTime.now());

        boolean success=true;
        if(success){
            payment.setStatut(PaiementStatut.TERMINE);
        }else {
            payment.setStatut(PaiementStatut.ECHOUE);
        }

        return paiementMapper.toDto(paiementRepository.save(payment));
    }

    @Override
    public Page<PaiementResponseDTO> paimentHistorique(Long userId, Pageable pageable) {
        return paiementRepository.findByUserId(userId,pageable)
                .map(paiementMapper::toDto);
    }



}
