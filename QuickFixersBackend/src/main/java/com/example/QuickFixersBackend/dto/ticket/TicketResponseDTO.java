package com.example.QuickFixersBackend.dto.ticket;

import com.example.QuickFixersBackend.enums.Priority;
import com.example.QuickFixersBackend.enums.Statut;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO implements Serializable {
    private Long id;
    private String titre;
    private String description;
    private Statut statut;
    private LocalDate dateCreation;
    private BigDecimal prix;
    private Long assignedToId;
    private String assignedToNom;
    private String assignedToPrenom;
    private String assignedToEmail;
    private Long serviceId;
    private Long createdById;

    // Priorité + logique associée (SLA / retard)
    private Priority priority;
    private String priorityLabel;
    private Integer slaHeures;
    private LocalDate echeance;
    private Boolean enRetard;
}
