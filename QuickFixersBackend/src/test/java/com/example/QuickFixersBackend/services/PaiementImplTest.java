package com.example.QuickFixersBackend.services;

import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import com.example.QuickFixersBackend.entity.Paiement;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.enums.PaiementStatut;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.mapper.PaiementMapper;
import com.example.QuickFixersBackend.repository.PaiementRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceImpl.PaiementImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaiementImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PaiementMapper paiementMapper;
    @Mock
    private PaiementRepository paiementRepository;
    @Mock
    private UserRepository userRepository;

    private PaiementImpl paiementService;

    @BeforeEach
    void setUp() {
        paiementService = new PaiementImpl(ticketRepository, paiementMapper, paiementRepository, userRepository);
    }

    @Test
    void payerTicket_ok() {
        User client = new User();
        client.setId(10L);
        client.setEmail("client@test.com");

        Ticket ticket = new Ticket();
        ticket.setId(5L);
        ticket.setCreatedBy(client);
        ticket.setStatut(Statut.OUVERT);

        PaiementRequestDTO dto = new PaiementRequestDTO();
        dto.setTicketId(5L);
        dto.setMontant(300);

        when(ticketRepository.findById(5L)).thenReturn(Optional.of(ticket));
        when(userRepository.findByEmail("client@test.com")).thenReturn(Optional.of(client));
        when(paiementRepository.existsByTicketAndStatut(ticket, PaiementStatut.TERMINE)).thenReturn(false);
        when(paiementMapper.toEntity(dto)).thenReturn(new Paiement());
        when(paiementRepository.save(any(Paiement.class))).thenReturn(new Paiement());
        when(paiementMapper.toDto(any(Paiement.class))).thenReturn(new PaiementResponseDTO());

        paiementService.creerPaiement(dto, "client@test.com");

        assertEquals(Statut.FERME, ticket.getStatut());

        ArgumentCaptor<Paiement> captor = ArgumentCaptor.forClass(Paiement.class);
        verify(paiementRepository).save(captor.capture());

        Paiement saved = captor.getValue();
        assertEquals(PaiementStatut.TERMINE, saved.getStatut());
        assertEquals(client, saved.getUser());
        assertEquals(ticket, saved.getTicket());
    }
}