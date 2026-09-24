package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.paiement.IncomeByDay;
import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import com.example.QuickFixersBackend.entity.Admin;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.Paiement;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.enums.PaiementStatut;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.mapper.PaiementMapper;
import com.example.QuickFixersBackend.repository.PaiementRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.PaiementInterface;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementImpl implements PaiementInterface {

    private final TicketRepository ticketRepository;
    private final PaiementMapper paiementMapper;
    private final PaiementRepository paiementRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PaiementResponseDTO creerPaiement(PaiementRequestDTO paiementRequestDTO, String email) {
        Ticket ticket = ticketRepository.findById(paiementRequestDTO.getTicketId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        Person person = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("email not found"));

        if(paiementRepository.existsByTicketAndStatut(ticket,PaiementStatut.TERMINE) && ticket.getStatut() == Statut.FERME){
            throw new RuntimeException("Ce ticket est déjà payé");
        }

        if(ticket.getCreatedBy() == null && !ticket.getAssignedTo().getEmail().equals(person.getEmail())){
            throw new RuntimeException("Vous ne pouvez payer que vos propres tickets");
        }

        Paiement payment = paiementMapper.toEntity(paiementRequestDTO);
        payment.setTicket(ticket);
        payment.setDateCreation(LocalDateTime.now());
        payment.setClient(person);

        boolean success=true;
        if(success){
            payment.setStatut(PaiementStatut.TERMINE);
            ticket.setStatut(Statut.FERME);
            ticketRepository.save(ticket);
        }else {
            payment.setStatut(PaiementStatut.ECHOUE);
        }

        return paiementMapper.toDto(paiementRepository.save(payment));
    }

    @Override
    public Page<PaiementResponseDTO> paimentHistorique(Person person, Pageable pageable) {
        if (person instanceof Admin) {
            return paiementRepository.findAll(pageable)
                    .map(paiementMapper::toDto);
        }
        if (person instanceof Client) {
            return paiementRepository.findByClient(person, pageable)
                    .map(paiementMapper::toDto);
        }
        if (person instanceof Support) {
            return paiementRepository.findByTicketAssignedTo(person, pageable)
                    .map(paiementMapper::toDto);
        }
        throw new RuntimeException("Access denied");
    }

    @Override
    public long countPayments(Person person) {
        if (person instanceof Admin) {
            return paiementRepository.count();
        }
        if (person instanceof Client) {
            return paiementRepository.countByClient(person);
        }
        throw new RuntimeException("Access denied");
    }

    @Override
    public List<IncomeByDay> incomeByday(Person person) {
        List<Object[]> rows;
        if (person instanceof Admin) {
            rows = paiementRepository.incomeByDay(PaiementStatut.TERMINE);
        } else if (person instanceof Support) {
            rows = paiementRepository.incomeByDayForSupport(PaiementStatut.TERMINE, person);
        } else {
            throw new RuntimeException("Access denied");
        }
        return rows.stream()
                .map(row -> new IncomeByDay(
                        ((java.sql.Date) row[0]).toLocalDate(),
                        ((java.math.BigDecimal) row[1]).doubleValue()))
                .toList();
    }
}