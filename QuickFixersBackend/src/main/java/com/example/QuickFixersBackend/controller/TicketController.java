package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.services.serviceInterfce.TicketInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@EnableMethodSecurity
public class TicketController {
    private final TicketInterface ticketInterface;

    @PostMapping("/{serviceId}/tickets")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<TicketResponseDTO> ajouterTicket(
            @PathVariable Long serviceId,
            @Valid  @RequestBody TicketRequestDTO ticketRequestDTO,
            @AuthenticationPrincipal Person person
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketInterface.ajouterTeckit(ticketRequestDTO, serviceId, person.getUsername())
                );
    }

    @PutMapping("/modifier/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<TicketResponseDTO> modifierTicket(
            @PathVariable Long id,
            @RequestBody TicketRequestDTO ticketRequestDTO) {

        return ResponseEntity.ok(
                ticketInterface.modifieTeckit(id, ticketRequestDTO)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT','SUPPORT')")
    public ResponseEntity<TicketResponseDTO> consulterTicket(
            @PathVariable Long id, @AuthenticationPrincipal Person person) {
        return ResponseEntity.ok(
                ticketInterface.consulterTeckit(id,person)
        );
    }

    @GetMapping("/tickets")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT', 'CLIENT')")
    public ResponseEntity<Page<TicketResponseDTO>> listerTickets(
            @AuthenticationPrincipal Person person,
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "5") int pageSize,
            @RequestParam (defaultValue = "dateCreation") String sortBy,
            @RequestParam (defaultValue = "asc") String  sortDir

    ) {
        Pageable pageable = creerPageable(pageNumber,pageSize, sortBy, sortDir);
        Page<TicketResponseDTO> rs = ticketInterface.listerTeckits(person,pageable);
        return ResponseEntity.ok(rs);
    }

    @PatchMapping("/statut/{ticketId}/{statut}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPORT')")
    public ResponseEntity<TicketResponseDTO> modifierStatut(
            @AuthenticationPrincipal Person person,
            @PathVariable Long ticketId, @PathVariable Statut statut) {
        return ResponseEntity.ok(ticketInterface.modifierStatut(person,ticketId, statut));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT','SUPPORT')")
    public ResponseEntity<Page<TicketResponseDTO>> filtrerParStatut(
            @AuthenticationPrincipal Person person,
            @PathVariable Statut statut,
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "5") int pageSize,
            @RequestParam (defaultValue = "dateCreation") String sortBy,
            @RequestParam (defaultValue = "asc") String  sortDir
    )
    {
        Pageable pageable = creerPageable(pageNumber,pageSize, sortBy, sortDir);
        Page<TicketResponseDTO> rs = ticketInterface.filtrerParStatut(person,statut,pageable);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/recherche")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT', 'CLIENT')")
    public ResponseEntity<Page<TicketResponseDTO>> rechercherTickets(
            @AuthenticationPrincipal Person person,
            @RequestParam String recherche,
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "5") int pageSize,
            @RequestParam (defaultValue = "dateCreation") String sortBy,
            @RequestParam (defaultValue = "asc") String  sortDir
            ) {
        Pageable pageable = creerPageable(pageNumber,pageSize, sortBy, sortDir);
        Page<TicketResponseDTO> rs = ticketInterface.rechercherTickets(person,recherche,pageable);
        return ResponseEntity.ok(rs);
    }

    private Pageable creerPageable(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }

    @GetMapping("/countTickets")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<Long> countTickets(
            @AuthenticationPrincipal Person person
    ){
        return ResponseEntity.ok(ticketInterface.countTickets(person));
    }

}