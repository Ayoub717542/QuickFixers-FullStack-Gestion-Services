package com.example.QuickFixersBackend.dto.ticket;

import com.example.QuickFixersBackend.enums.Statut;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private String titre;
    private String description;
    private Statut statut;
    private LocalDate dateCreation;
    private BigDecimal prix;
    private Long assignedToId;
    private Long serviceId;
    private Long createdById;
}
