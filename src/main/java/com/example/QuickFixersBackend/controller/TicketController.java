package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.services.serviceInterfce.TicketInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketInterface ticketInterface;

    @PostMapping("/ajouter")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TicketResponseDTO> ajouterTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        return ResponseEntity.ok(ticketInterface.ajouterTeckit(ticketRequestDTO));
    }

    @PutMapping("/modifier/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TicketResponseDTO> modifierTicket(
            @PathVariable Long id,
            @RequestBody TicketRequestDTO ticketRequestDTO) {

        return ResponseEntity.ok(
                ticketInterface.modifieTeckit(id, ticketRequestDTO)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TicketResponseDTO> consulterTicket(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ticketInterface.consulterTeckit(id)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<TicketResponseDTO>> listerTickets(
            Pageable pageable) {

        return ResponseEntity.ok(
                ticketInterface.listerTeckits(pageable)
        );
    }

    @PatchMapping("/assigner/{ticketId}/{supportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketResponseDTO> assignerTicket(
            @PathVariable Long ticketId,
            @PathVariable Long supportId) {

        return ResponseEntity.ok(
                ticketInterface.assignerTicket(ticketId, supportId)
        );
    }

    @PatchMapping("/statut/{ticketId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketResponseDTO> modifierStatut(
            @PathVariable Long ticketId,
            @RequestBody Statut statut) {

        return ResponseEntity.ok(
                ticketInterface.modifierStatut(ticketId, statut)
        );
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<TicketResponseDTO>> filtrerParStatut(
            @PathVariable Statut statut,
            Pageable pageable) {

        return ResponseEntity.ok(
                ticketInterface.filtrerParStatut(statut, pageable)
        );
    }

    @GetMapping("/recherche")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<TicketResponseDTO>> rechercherTickets(
            @RequestParam String recherche,
            Pageable pageable) {

        return ResponseEntity.ok(
                ticketInterface.rechercherTickets(recherche, pageable)
        );
    }
}
