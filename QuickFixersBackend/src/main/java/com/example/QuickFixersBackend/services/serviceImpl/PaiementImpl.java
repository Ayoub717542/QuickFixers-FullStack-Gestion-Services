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
import com.example.QuickFixersBackend.exception.BusinessException;
import com.example.QuickFixersBackend.exception.NotFoundException;
import com.example.QuickFixersBackend.mapper.PaiementMapper;
import com.example.QuickFixersBackend.repository.PaiementRepository;
import com.example.QuickFixersBackend.repository.TicketRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.PaiementInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.lowagie.text.PageSize;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    @CacheEvict(
            value = {"payments", "Countpayments", "inComeByday"},
            allEntries = true
    )    public PaiementResponseDTO creerPaiement(PaiementRequestDTO paiementRequestDTO, String email) {
        Ticket ticket = ticketRepository.findById(paiementRequestDTO.getTicketId())
                .orElseThrow(() -> new NotFoundException("Ticket not found"));
        Person person = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("email not found"));

        if(paiementRepository.existsByTicketAndStatut(ticket,PaiementStatut.TERMINE) && ticket.getStatut() == Statut.FERME){
            throw new BusinessException("Ce ticket est déjà payé");
        }

        if(ticket.getCreatedBy() == null && !ticket.getAssignedTo().getEmail().equals(person.getEmail())){
            throw new BusinessException("Vous ne pouvez payer que vos propres tickets");
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
    @Cacheable(
            value = "payments",
            key = "#person.getClass().getSimpleName() + ':'"
                    + " + #person.id + ':' + #pageable.toString()"
    )
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
        throw new AccessDeniedException("Access denied");
    }

    @Override
    @Cacheable(
            value = "Countpayments",
            key = "#person.getClass().getSimpleName() + ':' + #person.id"
    )
    public long countPayments(Person person) {
        if (person instanceof Admin) {
            return paiementRepository.count();
        }
        if (person instanceof Client) {
            return paiementRepository.countByClient(person);
        }
        throw new AccessDeniedException("Access denied");
    }

    @Override
    @Cacheable(
            value = "inComeByday",
            key = "#person.getClass().getSimpleName() + ':' + #person.id"
    )
    public List<IncomeByDay> incomeByday(Person person) {
        List<Object[]> rows;
        if (person instanceof Admin) {
            rows = paiementRepository.incomeByDay(PaiementStatut.TERMINE);
        } else if (person instanceof Support) {
            rows = paiementRepository.incomeByDayForSupport(PaiementStatut.TERMINE, person);
        } else {
            throw new AccessDeniedException("Access denied");
        }
        return rows.stream()
                .map(row -> new IncomeByDay(
                        toLocalDate(row[0]),
                        toDouble(row[1])))
                .toList();
    }

    /**
     * The type returned by MySQL's DATE() depends on the Hibernate/JDBC version
     * (Hibernate 6 gives java.time.LocalDate, older versions java.sql.Date),
     * so convert defensively instead of casting.
     */
    private static LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        if (value instanceof java.sql.Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        if (value instanceof java.util.Date date) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return LocalDate.parse(value.toString());
    }

    private static double toDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(value.toString());
    }

    @Override
    public byte[] genererRecu(Long paiementId) {
        Paiement payment = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new NotFoundException("Paiement not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A6, 20, 20, 20, 20);
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("QuickFixers — Reçu de paiement",
                    new Font(Font.HELVETICA, 16, Font.BOLD)));
            document.add(new Paragraph("------------------------------"));
            document.add(new Paragraph("Reçu n° " + payment.getId()));
            document.add(new Paragraph("Date : " + payment.getDateCreation()));
            document.add(new Paragraph("Client : " + payment.getClient().getNom() + " "
                    + payment.getClient().getPrenom()));
            document.add(new Paragraph("Email : " + payment.getClient().getEmail()));
            document.add(new Paragraph("Ticket n° : " + payment.getTicket().getId()));
            document.add(new Paragraph("Titre : " + payment.getTicket().getTitre()));
            document.add(new Paragraph("------------------------------"));
            document.add(new Paragraph("Montant : " + payment.getMontant() + " MAD",new Font(Font.BOLD)));
            document.add(new Paragraph("Statut : " + payment.getStatut()));

            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

}