package com.example.QuickFixersBackend.dto.ticket;

import com.example.QuickFixersBackend.enums.Statut;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    private String titre;
    private String description;
    private Statut statut;
    private double prix;
    private LocalDate dateCreation;
    private Long assignedToId;
    private Long serviceId;
    private Long createdById;
}