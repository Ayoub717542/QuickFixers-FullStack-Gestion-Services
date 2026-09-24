package com.example.QuickFixersBackend.services;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.enums.ServiceType;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.mapper.TicketMapper;
import com.example.QuickFixersBackend.repository.ServiceRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceImpl.EmailService;
import com.example.QuickFixersBackend.services.serviceImpl.TicketImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketImplTest {

    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private EmailService emailService;

    private TicketImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketImpl(ticketMapper, ticketRepository, userRepository, serviceRepository, emailService);
    }

    @Test
    void creerTicket_ok() {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitre("Pc en panne");
        dto.setDescription("Ecran noir");

        Client client = new Client();
        client.setId(10L);
        client.setEmail("client@test.com");

        ServiceEntity service = new ServiceEntity();
        service.setId(1L);
        service.setType(ServiceType.INFORMATIQUE);

        Support support = new Support();
        support.setId(5L);
        support.setEmail("support@test.com");

        when(ticketMapper.toEntity(dto)).thenReturn(new Ticket());
        when(userRepository.findByEmail("client@test.com")).thenReturn(Optional.of(client));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(userRepository.findSupportOrderByOpenTicketsAsc(ServiceType.INFORMATIQUE))
                .thenReturn(List.of(support));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());
        when(ticketMapper.toDto(any(Ticket.class))).thenReturn(new TicketResponseDTO());

        ticketService.ajouterTeckit(dto, 1L, "client@test.com");

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());

        Ticket saved = captor.getValue();
        assertEquals(Statut.OUVERT, saved.getStatut());
        assertEquals(client, saved.getCreatedBy());
        assertEquals(service, saved.getService());
        assertEquals(support, saved.getAssignedTo());
    }
}